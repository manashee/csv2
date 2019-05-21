package com.ashok;

import com.ashok.csv.header.HeaderImpl;
import com.ashok.csv.object.ObjectMaker;
import com.ashok.csv.typeinference.InferTypeImpl;
import com.ashok.entity.Stock;
import com.ashok.entity.Stock2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class Csv<T>
{

    public static void main( String[] args )
    {
        String option = args[0];
        String fileName = args[1];
        File input = new File ( fileName);

        try {
            if ( "Stock".equals(option)) {
                System.out.println("This demo will read 2 csv files containing Stock entity, each with 1 million rows and output row count");
                demoUsingStock(input);
            }
            else if ("Stock2".equals(option)) {
                System.out.println("This demo will read 2 csv files containing Stock2 entity, each with 1 million rows and output row count");
                demoUsingStock2(input);
            }
            else {
                System.out.println("Usage: java -jar target/csv-1.0-SNAPSHOT.jar Stock src/test/resources/TCS_Million.csv");
                System.out.println("Usage: java -jar target/csv-1.0-SNAPSHOT.jar Stock2 src/test/resources/TCS_MillionWithStockName.csv");
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void demoUsingStock (File input) throws Exception {
        Csv<Stock> csv = new Csv<>();
        Collection<Stock> stocks = csv.parse(input, Stock.class);
        System.out.println("File: " + input.getName()  + " RowCount: " + stocks.size());

        Collection<Stock> stocksDeDeup = csv.parseDeDup(input, Stock.class);
        System.out.println("File: " + input.getName() + " After Dedeuplication - RowCount: " + stocksDeDeup.size());
    }

    public static void demoUsingStock2 (File input)  throws Exception{
        Csv<Stock2> csv2 = new Csv<>();
        Collection<Stock2> stocks2 = csv2.parse(input, Stock2.class);
        System.out.println("File: " + input.getName() + " RowCount: " + stocks2.size());

        Collection<Stock2> stocks2DeDeup = csv2.parseDeDup(input, Stock2.class);
        System.out.println("File: " + input.getName() + " After Dedeuplication - RowCount: " + stocks2DeDeup.size());
    }

    public Collection<T> parse(File csv, Class<T> cls) throws IOException, InstantiationException, NoSuchFieldException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        return parseImpl(csv, cls, list);
    }


    public Collection<T> parseDeDup(File csv, Class<T> cls) throws IOException, InstantiationException, NoSuchFieldException, IllegalAccessException {
        Set<T> set = new HashSet<>();
        return parseImpl(csv, cls, set);
    }

    public Collection<T> parseImpl(File csv, Class<T> cls, Collection<T> collection) throws IOException, InstantiationException, NoSuchFieldException, IllegalAccessException {
        //this.cls = cls;

        FileReader fileReader = new FileReader(csv);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String [] headerRow = readALine(bufferedReader);
        String [] firstDataRow = readALine(bufferedReader);

        HeaderImpl headerImpl = new HeaderImpl(headerRow, firstDataRow, new InferTypeImpl());
        ObjectMaker<T> objectMaker = new ObjectMaker<>(headerImpl);
        T firstObj = toObj(cls, objectMaker, firstDataRow);
        collection.add (firstObj);

        String row ;
        while ((row = bufferedReader.readLine()) != null) {
            T obj = toObj(cls, objectMaker,  row.split(","));
            collection.add (obj);
        }

        return collection;
    }



    private String [] readALine ( BufferedReader bufferedReader) throws IOException {
        String row = bufferedReader.readLine();
        if ( null != row ) {
            return row.split(",");
        }
        return null;
    }


    public T toObj (Class<T> cls, ObjectMaker<T> objectMaker , String [] dataRow ) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        T instance = cls.newInstance();
        objectMaker.setFields(cls, instance, dataRow);
        return instance;
    }
}
