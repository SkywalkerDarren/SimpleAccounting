package io.github.skywalkerdarren.simpleaccounting.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;

public class CSVToData {
    private File mCSV;
    public CSVToData(File csv){
        mCSV = csv;
    }
    public List<Bill> getData(UUID accountId) throws IOException {
        FileReader reader = new FileReader(mCSV);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<Bill> bills = new ArrayList<>();
        for (CSVRecord record : csvParser) {
            Bill bill = new Bill();
            bill.setAccountId(accountId);
            //TODO 完善账单
            bill.setBalance(new BigDecimal(record.get(0)));
            bill.setDate(new DateTime());
            bill.setName(record.get(0));
            //bill.setTypeId(TypeLab.getInstance());
            //账单号及其他
            bill.setRemark(record.get(0));
            bills.add(bill);
        }
        return bills;
    }
}
