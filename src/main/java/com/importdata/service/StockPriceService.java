package com.importdata.service;

import com.importdata.domain.StockPrice;
import com.importdata.model.PriceDTO;
import com.importdata.model.StockPriceDTO;
import com.importdata.repository.StockPriceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockPriceService {

    @Autowired
    private StockPriceRepository stockPriceRepository;

    public void saveAll(List<StockPriceDTO> dtoList) {
        ArrayList<StockPrice> stockPricesList = new ArrayList<>();
        for(StockPriceDTO stockPriceDTO: dtoList) {
            StockPrice stockPrice = new StockPrice();
            BeanUtils.copyProperties(stockPriceDTO, stockPrice);
            stockPricesList.add(stockPrice);
        }
        Iterable iterable = stockPricesList;
        stockPriceRepository.saveAll(iterable);

    }

    public StockPriceDTO getStockPriceByStockCodeAndStockExchangeAndDateTime(StockPriceDTO stockPriceDTO) {
        String stockCode = stockPriceDTO.getStockCode();
        String stockExchange = stockPriceDTO.getStockExchange();
        String currentPrice = stockPriceDTO.getCurrentPrice();
        LocalDateTime dateTime = stockPriceDTO.getDateTime();
        StockPrice stockPrice = stockPriceRepository.getStockPriceByStockCodeAndStockExchangeAndAndDateTime(stockCode, stockExchange, dateTime);
        StockPriceDTO dbDTO = new StockPriceDTO();
        if(stockPrice == null) {
            return null;
        }
        BeanUtils.copyProperties(stockPrice, dbDTO);

        return dbDTO;
    }

    public List<StockPriceDTO> getStockPriceByStockCode(String stockCode) {
        List<StockPrice> stockPriceList = stockPriceRepository.getStockPricesByStockCode(stockCode);
        List<StockPriceDTO> stockPriceDTOList = new ArrayList();
        if(stockPriceList == null || stockPriceList.size() == 0) {
            return null;
        }

        for(StockPrice stockPrice: stockPriceList) {
            StockPriceDTO stockPriceDTO = new StockPriceDTO();
            BeanUtils.copyProperties(stockPrice, stockPriceDTO);
            stockPriceDTOList.add(stockPriceDTO);
        }
        return stockPriceDTOList;
    }


    public List<PriceDTO> getAvgPriceForCompanies(List<String> companyCodesList) {
        List<PriceDTO> priceDTOList = stockPriceRepository.getAvgPriceForCompanies(companyCodesList);

        return priceDTOList;
    }
}
