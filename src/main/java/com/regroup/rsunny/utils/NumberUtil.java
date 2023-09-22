package com.regroup.rsunny.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.regroup.rsunny.common.model.SaleDTO;

@Service
public class NumberUtil {

    public static String priceToStr(SaleDTO data) {
    	//boolean isCharter = "C".equals(data.getTradeType());	//전세
    	boolean isMonthly = "B".equals(data.getTradeType());	//월세
    	Long price = data.getPrice();
    	Long monthlyPrice = data.getMonthlyPrice();
    	//Long shortlyPrice = data.getShortPrice();
    	
		if(price != null && price > 0) {
			long p = price / 10000;
			long r = price % 10000;
			
			if(isMonthly && p > 0 && r > 0) {
				return String.format("%d억 %,d만원 / 월 %,d만원", p, r, monthlyPrice);
			}
			else if(isMonthly && p > 0) {
				return String.format("%d억원 / 월 %,d만원", p, monthlyPrice);
			}
			else if(isMonthly) {
				return String.format("보 %,d만원 / 월 %,d만원", r, monthlyPrice);
			}
			else if(p > 0 && r > 0) {
				return String.format("%d억 %,d만원", p, r);
			}
			else if(p > 0) {
				return String.format("%d억원", p);
			}
			else {
				return String.format("%,d만원", r);
			}
		}
		else {
			return "가격 미정";
		}
    }
    
    public static String priceToStr(String amount) {
    	Long price = Long.parseLong(amount);
    	
		if(price != null && price > 0) {
			long p = price / 10000;
			long r = price % 10000;
			
			if(p > 0 && r > 0) {
				return String.format("%d억 %,d만원", p, r);
			}
			else if(p > 0) {
				return String.format("%d억원", p);
			}
			else {
				return String.format("%,d만원", r);
			}
		}
		else {
			return "가격 미정";
		}
    }

    public static int m2ToPy(Double m2) {
    	if(m2==null || m2==0) return 0;
    	
		Double py = 0.3058;
    	return (int)(m2 * py);
    }

    public static String m2WithPy(Double m2) {
    	if(m2==null || m2==0) return "-㎡(-평)";
    	
		Double py = 0.3058;
    	return String.format("%.1f㎡(%d평)", m2, (int)(m2 * py));
    }

    public static String floorWithTotal(Integer floor, Integer totalFloor) {
    	return String.format("%s층 / 총 %s층", (floor==null)? "-" : String.valueOf(floor),(totalFloor==null)? "-" : String.valueOf(totalFloor));
    }

    public static String areaToString(Integer num) {
    	if(num==null || num==0) return "-";
    	
		return String.valueOf(num);
    }

    public static String areaToString(Double num) {
    	if(num==null || num==0) return "-";
    	
		return String.valueOf(num);
    }

    public static String getUniqueKey() {
    	DateTimeFormatter dtm = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    	String uuid = UUID.randomUUID().toString();
    	
    	return String.format("%s%su", LocalDateTime.now().format(dtm), uuid.substring(0, 7));
    }
    
    public static String getFormatPhone(String phone) {
    	if(phone==null) phone ="";
    	phone = phone.replaceAll("-", "");
    	if(phone.length()==11) {
    		return String.format("%s-%s-%s", phone.substring(0,3), phone.substring(3,7), phone.substring(7,11));
    	}
    	else if(phone.startsWith("02") && phone.length()==10) {
    		return String.format("%s-%s-%s", phone.substring(0,2), phone.substring(2,6), phone.substring(6,10));
    	}
    	else if(phone.length()==10) {
    		return String.format("%s-%s-%s", phone.substring(0,3), phone.substring(3,6), phone.substring(6,10));
    	}
    	else {
    		return phone;
    	}
    }

}
