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
import com.backend.billiards_management.services.image.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
    public Page<TableRes> getTables(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BilliardTable> tables = tableRepository.findAll(pageable);
        return tables.map(table -> modelMapper.map(table, TableRes.class));
    }

    @Override
    public TableRes createTable(BilliardTableReq req) {
        UploadImage uploadImage = null;

        if (req.getImageFile() != null && !req.getImageFile().isEmpty()) {
            uploadImage = uploadImageService.upload(req.getImageFile());
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

        if (req.getName() != null) {
            table.setName(req.getName());
        }

        if (req.getStatus() != null) {
            table.setStatus(req.getStatus());
        }

        if (req.getTableType() != null) {
            table.setTableType(req.getTableType());
        }

        if (req.getImageFile() != null && !req.getImageFile().isEmpty()) {

            UploadImage uploadImage = uploadImageService.upload(req.getImageFile());

            table.setUploadImage(uploadImage);
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
