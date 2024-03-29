package com.softwareapplication.remarket.service;

import com.softwareapplication.remarket.domain.*;
import com.softwareapplication.remarket.dto.AuctionDto;
import com.softwareapplication.remarket.dto.GroupPostDto;
import com.softwareapplication.remarket.dto.SecondHandDto;
import com.softwareapplication.remarket.dto.TenderPriceDto;
import com.softwareapplication.remarket.repository.SchedulerRepository;
import com.softwareapplication.remarket.repository.TenderPriceRepository;
import com.softwareapplication.remarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    @Autowired
    private final SchedulerRepository schedulerRepository;

    private final UserRepository userRepository;
    private final TenderPriceRepository tenderPriceRepository;

    @Autowired		// SchedulerConfig에 설정된 TaskScheduler 빈을 주입 받음
    private TaskScheduler scheduler;

    @Transactional
    public Long save(AuctionDto auctionDto) {
        User user = userRepository.findById(auctionDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + auctionDto.getUserId()));


        return schedulerRepository.save(auctionDto.toEntity(user)).getAuctionId();
    }

    @Transactional
    public Long saveTender(TenderPriceDto tenderPrice) {
        //User user = userRepository.findById(tenderPrice.getUser().getUserId())
        //        .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + auctionDto.getUserId()));
        Auction auction = schedulerRepository.findById(tenderPrice.getAuctionId()) .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. "));
        User user = userRepository.findById(tenderPrice.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return tenderPriceRepository.save(tenderPrice.toEntity(user,auction)).getTenderPriceId();
    }

    public List<TenderPrice> findByAuctionList(Long auctionId) {
        Auction auction = schedulerRepository.findById(auctionId) .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. "));
        return tenderPriceRepository.findTenderPriceByauctionId(auctionId);

    }

    public List<AuctionDto> findByList() {
        List<Auction> AuctionList =  schedulerRepository.findAll();
        List<AuctionDto> auctionDtoList = new ArrayList<>();
        for(Auction auction : AuctionList){
            AuctionDto auctionDto = new AuctionDto(auction);
            auctionDtoList.add(auctionDto);

        }
        return auctionDtoList;

    }

    public Auction findById(Long id) {
        return schedulerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id:" + id));

    }

    public TenderPrice findByTenderId(Long id) {

        return tenderPriceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id:" + id));

    }
    public AuctionDto findByDtoId(Long id) {
        Auction auction=schedulerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id:"+id));
        AuctionDto auctionDto = new AuctionDto(auction);
        return auctionDto;

    }
    @Transactional
    public Auction updateStatus(Long id){
        Auction auction= schedulerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id:"+id));
        auction.update("채택완료");
        //secondHand.update(secondHandDto.toEntity());
        return schedulerRepository.save(auction);
    }
    @Transactional
    public Auction updateStatusAndPrice(Long id, int price){
        Auction auction= schedulerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id:"+id));
        auction.update("채택완료",price);
        //secondHand.update(secondHandDto.toEntity());
        return schedulerRepository.save(auction);
    }
    @Transactional
    public void auctionScheduler(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dueDate, AuctionDto auctionDto) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                auctionDto.setStatus("기한마감");
                updateStatus(auctionDto);
            }
        };
        scheduler.schedule(r, dueDate);

    }
    @Transactional
    public void updateStatus(AuctionDto auctionDto){
        User user =userRepository.findByUserId(auctionDto.getUserId());
        schedulerRepository.save(auctionDto.toEntity(user));
    }

}
