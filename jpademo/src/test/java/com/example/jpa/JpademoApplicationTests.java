package com.example.jpa;

import com.example.jpa.bean.ApiSendRecord;
import com.example.jpa.dao.ApiSendRecordDAO;
import com.example.jpa.enums.DataTrans;
import com.example.jpa.enums.HandStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpademoApplicationTests {
	@Autowired
	private ApiSendRecordDAO dao;


	@Test
	public void dao() {
		List< ApiSendRecord > list = dao.findByBankNo("1705050094000020");
		for (ApiSendRecord apiSendRecord:list) {
			System.out.println(apiSendRecord.toString());
		}
	}

	@Test
	public void findByDataTransAndHandStatus(){
		List< ApiSendRecord > list = dao.findByDataTransAndHandStatus(DataTrans.BANK2CHEDAI, HandStatus.SUCCESS);
		for (ApiSendRecord apiSendRecord:list) {
			System.out.println(apiSendRecord.toString());
		}
	}

}
