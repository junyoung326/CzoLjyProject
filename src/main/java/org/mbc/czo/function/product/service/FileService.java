package org.mbc.czo.function.product.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    // 서버에 업로드된 파일 데이터를 받아서, 중복되지 않는 새 파일 이름을 생성해 특정 경로에 저장하고, 저장된 파일명을 반환하는 목적
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출
        String savedFileName = uuid.toString() + extension; // 파일명 겹치지 않게 설정
        String fileUploadFullUrl = uploadPath + "/" + savedFileName; // 파일이 저장될 전체 경로
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        // FileOutputStream = 바이트 단위로 파일에 쓰는 Java 클래스.
        //fileUploadFullUrl → 저장할 전체 경로 + 파일 이름 (예: "C:/shop/item/이미지.jpg").
        //이 줄에서 "이 위치에 파일을 만들 준비"를 함.
        //만약 해당 경로에 같은 이름의 파일이 있으면 덮어씌움.
        fos.write(fileData);
        // fileData → 실제 파일 내용(바이트 배열).
        //이 줄에서 준비된 파일에 바이트 데이터를 씀.
        fos.close();
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
