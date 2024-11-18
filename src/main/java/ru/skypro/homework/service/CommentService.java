package ru.skypro.homework.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;

    public CommentService(CommentRepository commentRepository, AdRepository adRepository) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
    }

    public Collection<CommentEntity> getComments(Long adId) {
        Collection<CommentEntity> listComment = commentRepository.findByAd_Id(adId);
        if(!listComment.isEmpty()){
            return listComment;
        }
        throw new NullPointerException("No comment found for ad id " + adId);

    }
    public void addComment(Long adId, CommentEntity comment) {
        Optional<AdEntity> ad = adRepository.findById(adId);
        if(ad.isPresent()){
            CommentEntity commentNew = new CommentEntity();
            commentNew.setAd(ad.get());
            commentNew.setText(comment.getText());
            commentNew.setId(comment.getId());
            commentNew.setIdUser(comment.getIdUser());
            commentNew.setImageUserCreateComment(comment.getImageUserCreateComment());
            commentNew.setDateCreateComment(comment.getDateCreateComment());
            commentNew.setImageUserCreateComment(comment.getImageUserCreateComment());
            commentRepository.save(commentNew);
        }
        throw new NullPointerException("No comment found for ad id " + adId);

    }
    public CommentEntity removeComment(Long adId, Long commentId){
        Optional<AdEntity> ad = adRepository.findById(adId);
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if(ad.isPresent() && comment.isPresent() && comment.get().getAd().getId().equals(ad.get().getId())){
            commentRepository.delete(comment.get());
            return comment.get();
        }

        throw new NullPointerException("No comment found for id" + commentId +" or for ad id " + adId);
    }

    public CommentEntity updateComment(Long commentId, CommentEntity commentUpdate){
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if(comment.isPresent()){
            comment.get().setId(commentUpdate.getId());
            comment.get().setAd(comment.get().getAd());
            comment.get().setText(commentUpdate.getText());
            comment.get().setIdUser(commentUpdate.getIdUser());
            comment.get().setImageUserCreateComment(commentUpdate.getImageUserCreateComment());
            comment.get().setDateCreateComment(commentUpdate.getDateCreateComment());
            comment.get().setImageUserCreateComment(commentUpdate.getImageUserCreateComment());
            commentRepository.save(comment.get());
            return comment.get();
        }
        throw new NoSuchElementException();
    }
}
