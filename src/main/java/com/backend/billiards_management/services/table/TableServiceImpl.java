package com.backend.billiards_management.services.table;

import com.backend.billiards_management.dtos.request.billiard_table.BilliardTableReq;
import com.backend.billiards_management.dtos.request.billiard_table.UpdateTableReq;
import com.backend.billiards_management.dtos.response.billiard_table.TableRes;
import com.backend.billiards_management.dtos.response.image.UploadRes;
import com.backend.billiards_management.entities.billiard_table.BilliardTable;
import com.backend.billiards_management.entities.image.UploadImage;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.TableRepository;
import com.backend.billiards_management.services.uploadImage.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;

    private final ModelMapper modelMapper;

    private final UploadImageService uploadImageService;
    @Override
    public TableRes getTableById(int id) {
        return modelMapper.map(tableRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"Table not found")), TableRes.class);
    }

    @Override
    public Page<TableRes> getTables(Pageable pageable) {
        Page<BilliardTable> tables = tableRepository.findAll(pageable);

        return tables.map(table -> {
            TableRes res = modelMapper.map(table, TableRes.class);
            res.setImageUrl(table.getUploadImage().getImageUrl()); // set thêm nếu cần override
            return res;
        });
    }

    @Override
    public TableRes createTable(BilliardTableReq req) {
        UploadImage uploadImage = null;
        MultipartFile imageFile = req.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            uploadImage = uploadImageService.uploadImageToCloudinary(imageFile);
        }
        BilliardTable table = BilliardTable.builder()
                .name(req.getName())
                .status(req.getStatus())
                .tableType(req.getTableType())
                .uploadImage(uploadImage)
                .build();

        tableRepository.save(table);
        TableRes res = modelMapper.map(table, TableRes.class);
        if(uploadImage != null) {
            res.setImageUrl(uploadImage.getImageUrl());
        }
        return res;
    }

    @Override
    public TableRes updateTable(UpdateTableReq req) {
        BilliardTable table = tableRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Table not found"));
        MultipartFile imageFile = req.getImageFile();

        if (req.getName() != null) {
            table.setName(req.getName());
        }

        if (req.getStatus() != null) {
            table.setStatus(req.getStatus());
        }

        if (req.getTableType() != null) {
            table.setTableType(req.getTableType());
        }

        if (imageFile != null && !imageFile.isEmpty()) {

            UploadImage uploadImage = uploadImageService.uploadImageToCloudinary(imageFile);

            if (table.getUploadImage() != null) {
                uploadImageService.deleteImageFromCloudinary(table.getUploadImage().getPublicId());
            }

            table.setUploadImage(uploadImage);
        }
        else if (imageFile == null) {

            table.setUploadImage(table.getUploadImage());
        }

        table = tableRepository.save(table);

        TableRes res = modelMapper.map(table, TableRes.class);

        if (table.getUploadImage() != null) {
            res.setImageUrl(table.getUploadImage().getImageUrl());
        }

        return res;
    }

    @Override
    public void deleteTable(int id) {
        BilliardTable table = tableRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Table not found"));
        tableRepository.delete(table);
    }
}
