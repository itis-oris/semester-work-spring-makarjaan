package com.makarova.service.impl;

import com.makarova.entity.Favorite;
import com.makarova.entity.User;
import com.makarova.repository.FavoriteRepository;
import com.makarova.repository.UserRepository;
import com.makarova.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    @Override
    public boolean isApartmentInFavorites(Long userId, Long apartmentId, String dealType) {
        return favoriteRepository.existsByUser_IdAndApartmentIdAndDealType(
                userId,
                apartmentId,
                dealType
        );
    }

    @Override
    @Transactional
    public boolean toggleFavorite(Long userId, Long apartmentId, String dealType) {
        var favoriteOpt = favoriteRepository.findByUser_IdAndApartmentIdAndDealType(
                userId,
                apartmentId,
                dealType
        );

        if (favoriteOpt.isPresent()) {
            favoriteRepository.delete(favoriteOpt.get());
            return false;
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Favorite favorite = Favorite.builder()
                    .user(user)
                    .apartmentId(apartmentId)
                    .dealType(dealType)
                    .build();

            favoriteRepository.save(favorite);
            return true;
        }
    }
}