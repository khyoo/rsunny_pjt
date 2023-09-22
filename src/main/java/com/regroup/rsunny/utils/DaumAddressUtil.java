package com.regroup.rsunny.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regroup.rsunny.forsale.model.address.DaumAddressDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaumAddressUtil {
	
	//다음 주소찾기 API.
	public static DaumAddressDTO getAddress(String apiKey, String address) {
        try {
            HttpClient httpclient = HttpClientBuilder.create().build();
            
            URIBuilder builder = new URIBuilder("https://dapi.kakao.com/v2/local/search/address.json");
            builder.setParameter("query", address);

            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.addHeader("Content-type", "application/json");
            httpGet.addHeader("Authorization", String.format("KakaoAK %s", apiKey));
            
            HttpResponse response = httpclient.execute(httpGet);
            int responseCode = response.getStatusLine().getStatusCode();
            log.info("★★★★★★주소 조회 : [ResponseCode={}] {}", responseCode, address);
            
            if(responseCode != 200) {
            	return DaumAddressDTO.of(responseCode, String.format("주소 정보 조회에 실패하였습니다.\n[ResponseCode=%d]", responseCode));
            }
            
            String responseText = EntityUtils.toString(response.getEntity());
			log.debug("...responseText:\n{}", responseText);

		    InputStream is = new ByteArrayInputStream(responseText.getBytes("UTF-8"));

			ObjectMapper mapper = new ObjectMapper();
			
			DaumAddressDTO data = mapper.readValue(is, DaumAddressDTO.class);
			log.info("Daum.result.count={}", data.getMeta().getTotalCount());
			
			if(data.getMeta().getTotalCount() > 0) {
				data.setCode(0);
				data.setMessage("주소 정보 조회 성공.");
			}
			else {
				data.setCode(-1);
				data.setMessage("다음 주소 조회 건수=0");
			}
			
			return data;
			
        } catch (Exception e) {
        	e.printStackTrace();
        	return DaumAddressDTO.of(-99, String.format("주소 정보 조회에 실패하였습니다. 관리자에게 문의하세요.\n[Error=%s]", e.getMessage()));
        }
	}

}
