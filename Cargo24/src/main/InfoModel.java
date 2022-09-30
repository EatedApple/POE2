package main;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InfoModel {
	String[] sort0_1 = { "다마스", "라보" };
	String[] sort1_3 = { "차종확인", "전체", "카고", "윙바디", "탑", "초장축", "호루", "냉동탑", "리프트", "리프트윙", "냉장윙", "리프트호", "리프트탑", "초장축호",
			"초장축탑", "초장축윙", "냉장탑", "초장축호리", "초장축탑리", "초장축윙리", "초장축냉동탑", "초냉장윙리", "냉장탑리", "초냉장탑리", "초장축리", "냉동윙", "냉동윙리",
			"냉장윙리", "초장축냉동윙", "초냉동윙리", "초장축냉장윙", "초냉동탑리", "초장축냉장탑" };
	String[] sort5_25 = {"차종확인", "전체", "카고", "윙바디", "탑", "축카고", "호루", "냉동탑", "플러스카", "플축카고", "윙축", "리프트", "플러스윙",
			"리프트윙", "플축윙", "냉장윙", "냉장윙축", "리프트호", "리프트탑", "냉장탑", "냉장리플윙", "냉동리플윙", "플축윙리", "플러스윙리", "냉장탑리", "냉장윙축리",
			"냉동윙축리", "축카고리", "윙축리", "플러스리", "냉동윙", "냉동윙리", "냉장윙리", "냉동윙축", "냉장플윙", "냉동플윙", "냉동플축윙리", "냉장플축윙리", "냉동탑플축",
			"냉동탑플", "냉동탑플리", "냉동탑플축리", "냉동탑리", "플축카리", "냉동탑축", "플호리", "플축호리", "플축호루", "플러스호", "플러스탑", "플축탑", "플축탑리",
			"냉장탑플", "냉장탑플축", "냉장탑플축리", "냉장탑플리", "호루축", "냉장플축윙", "냉동플축윙", "플러스탑리" };
	String[] arrivalArray = {"선택", "당착", "내착", "월착"};
	String[] paymentMethodArray = {"지불방식", "인수증", "선/착불", "수수료확인", "카드"};
	Double[] tonArray = new Double[] { 0.3, 0.5, 1.0, 1.4, 2.5, 3.5, 5.0, 8.0, 11.0, 14.0, 15.0, 18.0, 25.0 };
	HashMap<String, String[]> tonAndSortDictionary = new HashMap<String, String[]>();
	public String type; // search or insert
	public String load_addr; // 상차지
	public String alight_addr; // 하차지
	
	public double ton = 1; // 톤수
	public String ton_info = "";
	public int ton_idx = 0;
	
	public String car_sort = "차종확인"; // 차종
	public int car_sort_idx = 0;
	
	public String arrival; // 도착
	public int arrival_idx = 0;
	
	public int count = 1; // 대수
	
	public String payment_method = "선/찰불"; // 지불 방식
	public int payment_method_idx = 2;
	
	public int price = 0;
	public int commission = 0;
	public String freight_info = "";
	public String cargo_information = "";
	public String zin_36 = "";

	public String amount_by_distance;
	public byte[] capture_image;

	public String[] car_sort_array;
	
	public String mixed_loading = "";
	public String reserved = ""; // '' or 예약

	public int time;
	public InfoModel(String data) {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonObj = (JSONObject) obj;
		this.type = (String) jsonObj.get("type");
		
		if (jsonObj.get("time") != null) {
			this.time = Integer.parseInt(String.valueOf(jsonObj.get("time")));
		}
		
		if (jsonObj.get("load_addr") != null) {
			this.load_addr = (String) jsonObj.get("load_addr");
		}
		if (jsonObj.get("alight_addr") != null) {
			this.alight_addr = (String) jsonObj.get("alight_addr");
		}

		if (jsonObj.get("car_ton") != null) {
			this.ton = Double.parseDouble(String.valueOf(jsonObj.get("car_ton")).replaceAll("[^\\d.]", ""));
			this.ton_idx = Arrays.asList(tonArray).indexOf(ton);
			DecimalFormat format = new DecimalFormat("0.##");
			this.ton_info = format.format((this.ton * 1.1));
		}

		if (ton < 1) {
			this.car_sort_array = this.sort0_1;
		} else if (ton >= 1 && ton < 5) {
			this.car_sort_array = this.sort1_3;
		} else {
			this.car_sort_array = this.sort5_25;
		}

		if (jsonObj.get("car_sort") != null) {
			this.car_sort = (String) jsonObj.get("car_sort");
			this.car_sort_idx = Arrays.asList(car_sort_array).indexOf(car_sort);
			if (this.car_sort_idx == -1) {
				this.car_sort_idx = 0;
				this.car_sort = "해당 톤수에 " + this.car_sort + "x";
			}
		} 

		if (jsonObj.get("arrival") != null) {
			this.arrival = (String) jsonObj.get("arrival");
			this.arrival_idx = Arrays.asList(arrivalArray).indexOf(this.arrival);
			if (this.arrival_idx == -1) {
				this.arrival_idx = 0;
			}
		}

		if (jsonObj.get("count") != null) {
			this.count = (int) jsonObj.get("count");
		}

		if (jsonObj.get("payment_method") != null) {
			this.payment_method = (String) jsonObj.get("payment_method");
			this.payment_method_idx = Arrays.asList(paymentMethodArray).indexOf(this.payment_method);
			if (this.payment_method_idx == -1) {
				this.payment_method_idx = 0;
			}
		}
		
		if (jsonObj.get("price") != null) {
			this.price = Integer.parseInt(String.valueOf(jsonObj.get("price")));
		}
		
		if (jsonObj.get("commission") != null) {
			this.commission = Integer.parseInt(String.valueOf(jsonObj.get("commission")));
		}
		
		if (jsonObj.get("freight_info") != null) {
			this.freight_info = (String) jsonObj.get("freight_info");
		}
		
		if (jsonObj.get("mixed_loading") != null) {
			this.mixed_loading = (String) jsonObj.get("mixed_loading");
		}
		
		if (jsonObj.get("zin_36") != null) {
			this.zin_36 = (String) jsonObj.get("zin_36");
		}

		if (jsonObj.get("reserved") != null) {
			this.reserved = (String) jsonObj.get("reserved");
		}
	}
}