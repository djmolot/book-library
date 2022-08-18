package company.name.service;

import company.name.models.Reader;
import company.name.models.dto.ReaderRequestDto;
import company.name.models.dto.ReaderResponseDto;

import java.util.List;

public interface ReaderService {
    void createNewReader(ReaderRequestDto requestDto);

    List<ReaderResponseDto> getAll();

}
