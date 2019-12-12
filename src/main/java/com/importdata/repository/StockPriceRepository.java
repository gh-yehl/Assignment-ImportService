package com.importdata.repository;

import com.importdata.domain.StockPrice;
import com.importdata.model.PriceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StockPriceRepository extends JpaRepository<StockPrice,Long> {

    @Override
    <S extends StockPrice> List<S> saveAll(Iterable<S> entities);

    //public StockPrice getStockPriceByStockCodeAndStockExchangeAndCurrentPrice(String stockCode,String stockExchange, String currentPrice);
    public StockPrice getStockPriceByStockCodeAndStockExchangeAndAndDateTime(String stockCode,String stockExchange, LocalDateTime dateTime);


    @Query(value = "select * from stockprice s where s.stockCode = ?1 order by datetime asc", nativeQuery = true)
    public List<StockPrice> getStockPricesByStockCode(String stockCode) ;


    @Query(value = "SELECT new com.importdata.model.PriceDTO(s.dateTime,avg(s.currentPrice)) " +
            "from StockPrice s where s.stockCode in (?1) GROUP BY s.dateTime ORDER BY s.dateTime asc")
    public List<PriceDTO> getAvgPriceForCompanies(List<String> companyList );

}
