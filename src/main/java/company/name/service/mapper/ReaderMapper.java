package company.name.service.mapper;

import company.name.models.Reader;
import company.name.models.dto.ReaderRequestDto;
import company.name.models.dto.ReaderResponseDto;

public class ReaderMapper {
    public ReaderResponseDto mapToDto(Reader reader) {
        ReaderResponseDto responseDto = new ReaderResponseDto();
        responseDto.setId(reader.getId());
        responseDto.setName(reader.getName());
        return responseDto;
    }

    public Reader mapToModel(ReaderRequestDto requestDto) {
        Reader reader = new Reader();
        reader.setName(requestDto.getName());
        return reader;
    }
}
