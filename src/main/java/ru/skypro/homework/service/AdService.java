package ru.skypro.homework.service;

import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.UserController;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AdService {

    private final AdRepository adRepository;
    private Logger logger = LoggerFactory.getLogger(AdService.class);


    public AdService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    public Collection<AdEntity> getAllAd(){
        return adRepository.findAll();
    }
    public boolean addAd(AdEntity ad, MultipartFile image){
        Optional<AdEntity> adExisting = adRepository.findById(ad.getId());
        try {
            if(ad != null && image != null && !adExisting.isPresent()) {
                AdEntity adNew = new AdEntity();
                UserEntity user = ad.getUser();
                adNew.setId(ad.getId());
                adNew.setTarget(ad.getTarget());
                adNew.setTitle(ad.getTitle());
                adNew.setPrice(ad.getPrice());
                adNew.setLastNameUser(user.getLastName());
                adNew.setFirstNameUser(user.getFirstName());
                adNew.setNameUser(user.getUserName());
                adNew.setPhoneUser(user.getPhone());
                adNew.setUser(user);


                String fileName = Instant.now().toEpochMilli() + "_" + image.getOriginalFilename();
                Path path = Paths.get("uploads", fileName);
                Files.write(path, image.getBytes());
                ad.setImageFileName(fileName);
                adRepository.save(adNew);
                return true;
            }
        }catch (IOException e){
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e);

        }
        return false;
    }
    public AdEntity getAdById(Long id){
        Optional<AdEntity> ad = adRepository.findById(id);
        if(ad.isPresent()) {
            return ad.get();
        }
        throw new NoSuchElementException();
    }

    public AdEntity removeAd(Long id){
        Optional<AdEntity> removeAd = adRepository.findById(id);
        if(removeAd.isPresent()) {
            adRepository.delete(removeAd.get());
            return removeAd.get();
        }
        throw new NoSuchElementException();
    }
    public AdEntity updateAd(AdEntity ad, Long id){
        Optional<AdEntity> adExisting = adRepository.findById(id);
        if(adExisting.isPresent()) {
            AdEntity adNew = new AdEntity();
            UserEntity user = ad.getUser();
            adNew.setId(ad.getId());
            adNew.setTarget(ad.getTarget());
            adNew.setTitle(ad.getTitle());
            adNew.setPrice(ad.getPrice());
            adNew.setImageFileName(ad.getImageFileName());
            adNew.setLastNameUser(user.getLastName());
            adNew.setFirstNameUser(user.getFirstName());
            adNew.setNameUser(user.getUserName());
            adNew.setPhoneUser(user.getPhone());
            adNew.setUser(user);
            adRepository.save(adNew);
            return adNew;
        }
        throw new NoSuchElementException();
    }

    public void updateImageAd(Long id, MultipartFile image){
        AdEntity ad = adRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Объявление не найдено"));
        try {
            if (image != null && !image.isEmpty()) {
                String fileName = Instant.now().toString() + "-" + image.getOriginalFilename();
                FileOutputStream outputStream = new FileOutputStream("images/" + fileName);
                outputStream.write(image.getBytes());
                outputStream.close();

                ad.setImageFileName(fileName);
                adRepository.save(ad);
            }
        }catch (IOException e){
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }


    }



}
