package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.NewsCardRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.NewsCardResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.NewsCard;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.NewsCardMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.NewsCardRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.NewsCardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsCardServiceImplement implements NewsCardService {

    private static final String NEWS_CARD_WITH_ID_NOT_FOUND = "Новостная карточка с указанным идентификатором не найдена - %s";

    private final NewsCardMapper newsCardMapper;
    private final NewsCardRepository newsCardRepository;

    @Override
    public NewsCardResponseDto create(@NonNull NewsCardRequestDto newsCardRequestDto) {
        var newsCard = newsCardMapper.toEntity(newsCardRequestDto);

        return newsCardMapper.toResponseDto(newsCardRepository.save(newsCard));
    }

    @Override
    public NewsCardResponseDto patch(@NonNull Long id, @NonNull NewsCardRequestDto newsCardRequestDto) {
        var newsCardForUpdate = getEntityById(id);

        var newsCard = newsCardMapper.toPatchEntity(newsCardRequestDto, newsCardForUpdate);

        return newsCardMapper.toResponseDto(newsCardRepository.save(newsCard));
    }

    @Override
    public NewsCard getEntityById(@NonNull Long id) {
        return newsCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NEWS_CARD_WITH_ID_NOT_FOUND, id)));
    }

    @Override
    public NewsCardResponseDto getResponseById(@NonNull Long id) {
        return newsCardMapper.toResponseDto(getEntityById(id));
    }

    @Override
    public Page<NewsCardResponseDto> getAll(@NonNull Pageable pageable) {
        return newsCardRepository.findAll(pageable)
                .map(newsCardMapper::toResponseDto);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        newsCardRepository.delete(newsCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NEWS_CARD_WITH_ID_NOT_FOUND, id))));
    }
}
