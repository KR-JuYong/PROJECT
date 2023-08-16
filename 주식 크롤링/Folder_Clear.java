package project;

import org.apache.commons.io.FileUtils; 
import java.io.File;
import java.io.IOException;

public class Folder_Clear {
    public static void main(String[] args) throws IOException {
    FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 거래상위"));
    FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 상승"));
    FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 하락"));
    FileUtils.cleanDirectory(new File("D:/workspace/java/base/src/project/주식 차트/TOP 시가총액 상위"));
    }
}
