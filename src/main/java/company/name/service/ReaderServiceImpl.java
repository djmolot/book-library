package company.name.service;

import company.name.dao.ReaderDao;
import company.name.models.Reader;
import company.name.models.dto.ReaderRequestDto;
import company.name.models.dto.ReaderResponseDto;
import company.name.service.mapper.ReaderMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReaderServiceImpl implements ReaderService {
    private final ReaderDao readerDao;
    private final ReaderMapper readerMapper;

    public ReaderServiceImpl(ReaderDao readerDao, ReaderMapper readerMapper) {
        this.readerDao = readerDao;
        this.readerMapper = readerMapper;
    }

    @Override
    public void createNewReader(ReaderRequestDto requestDto) {
        Reader reader = readerMapper.mapToModel(requestDto);
        readerDao.add(reader);
    }

    @Override
    public List<ReaderResponseDto> getAll() {
        return readerDao.getAll().stream()
                .map(readerMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
