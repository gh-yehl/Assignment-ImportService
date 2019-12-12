package com.importdata.controller;

import com.importdata.model.PriceDTO;
import com.importdata.model.StockPriceDTO;
import com.importdata.service.StockPriceService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin(maxAge = 3600)
@RestController
public class StockPriceController {

    @Autowired
    private StockPriceService stockPriceService;

    /**
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
    public StockPriceDTO uploadExcel(@RequestParam("excelFile") MultipartFile file, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {

        StockPriceDTO resultDTO = new StockPriceDTO();
        StringBuilder buffer = new StringBuilder("uploadImportExcel start...\n");
        Workbook workbook = null;
        try {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            if (fileName.matches("^.+\\.(?i)(xls)$")) { // EXCEL2003
                workbook = new HSSFWorkbook(inputStream);
            }
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) { // EXCEL2007
                workbook = new XSSFWorkbook(inputStream);
            }

            if (workbook != null) {
                Sheet sheet = workbook.getSheetAt(0);
                //lastRowNum starts from 0,  so totalRows should be lastRowNum + 1
                int totalRows = sheet.getLastRowNum() + 1;
                if (totalRows == 0) {
                    return resultDTO;
                }

                List<StockPriceDTO> stockPriceDTOList = new ArrayList();
                for (int i = 0; i < totalRows; i++) {
                    StockPriceDTO stockPriceDTO = new StockPriceDTO();
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        Cell cell1 = row.getCell(0); // first column - String stockCode
                        Cell cell2 = row.getCell(1); // second column - String stockExchange
                        Cell cell3 = row.getCell(2); // third column - BigDecimal currentPrice
                        Cell cell4 = row.getCell(3); // forth column - LocalDateTime date
                        Cell cell5 = row.getCell(4); // fifth column - LocalDateTime time

                        if (cell1 == null || cell2 == null || cell3 == null || cell4 == null || cell5 == null) {
                            break;
                        }

                        stockPriceDTO.setStockCode(cell1.getStringCellValue().trim().replaceAll("\\u00A0", ""));
                        stockPriceDTO.setStockExchange(cell2.getStringCellValue().trim());
                        stockPriceDTO.setCurrentPrice(String.valueOf(cell3.getNumericCellValue()));

                        String cell4Str = cell4.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                        String cell5Str = cell5.getStringCellValue();
                        String dataTime_newRecord = cell4Str.trim() + " " + cell5Str.trim();
                        LocalDateTime stockDataTime_newRecord = LocalDateTime.parse((CharSequence) dataTime_newRecord, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        stockPriceDTO.setDateTime(stockDataTime_newRecord);

                        StockPriceDTO dbDTO = stockPriceService.getStockPriceByStockCodeAndStockExchangeAndDateTime(stockPriceDTO);
                        if (dbDTO != null) {
                            String col = "currentPrice";
                            BeanUtils.copyProperties(dbDTO, stockPriceDTO, col);
                        }
                        stockPriceDTOList.add(stockPriceDTO);

                        //prepare return Data - resultDTO will be returned
                        if(resultDTO.getStockCode() == null) {
                            String[] ignoreCopyColumns = {"id", "currentPrice", "dateTime"};
                            BeanUtils.copyProperties(stockPriceDTO, resultDTO, ignoreCopyColumns);
                            resultDTO.setFromDateTime(dataTime_newRecord);
                            resultDTO.setToDateTime(dataTime_newRecord);
                        }

                        else {
                            LocalDateTime fromDateTime_Temp = LocalDateTime.parse((CharSequence) resultDTO.getFromDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            LocalDateTime toDateTime_Temp = LocalDateTime.parse((CharSequence) resultDTO.getToDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            if(fromDateTime_Temp.isAfter(stockDataTime_newRecord)) {
                                resultDTO.setFromDateTime(dataTime_newRecord);
                            }
                            if(toDateTime_Temp.isBefore(stockDataTime_newRecord)) {
                                resultDTO.setToDateTime(dataTime_newRecord);
                            }
                        }

                        resultDTO.setTotalRows(stockPriceDTOList.size());
                    }
                }

                //process all record into DB
                if (stockPriceDTOList.size() > 0) {
                    stockPriceService.saveAll(stockPriceDTOList);
                }

            } else {
                return resultDTO;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return resultDTO;
    }

    /**
     *
     * @param stockCode
     */
    @RequestMapping("/getStockPriceList")
    public List<StockPriceDTO> getStockPriceList(@RequestParam("stockCode") String stockCode) {
        List<StockPriceDTO> stockPriceDTOList = stockPriceService.getStockPriceByStockCode(stockCode);
        return stockPriceDTOList;
    }


    /**
     *
     * @param companyCodesList
     * @return
     */
    @RequestMapping("/getAvgPriceList")
    public List<PriceDTO> getAvgPriceForCompanies(@RequestParam("companyCodesList") List<String> companyCodesList) {

        List<PriceDTO> priceDTOList = stockPriceService.getAvgPriceForCompanies(companyCodesList);
        return priceDTOList;
    }


}
