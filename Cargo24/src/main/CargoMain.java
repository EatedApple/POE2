package main;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.simple.JSONObject;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.win32.W32APIOptions;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import com.sun.jna.win32.StdCallLibrary;

public class CargoMain extends JFrame {
	public static final int EM_REPLACESEL = 0x00c2;
	public static final int EM_SETMODIFY = 0x00b9;
	public static final int WM_SETTEXT = 0x000c;
	public static final int WM_GETTEXT = 0x000D;
	public static final int EM_SETSEL = 0x00b1;
	public static final int WM_KEYDOWN = 0x100;
	public static final int WM_KEYUP = 0x101;
	public static final int WM_CHAR = 0x0102;
	public static final int VK_RETURN = 0x0D;
	public static final int VK_ENTER = 0x0D;
	public static final int VK_TAB = 0x09;
	public static final int WM_SETFOCUS = 0x0007;
	public static final int WM_KILLFOCUS = 0x0008;
	public static final int CB_SETCURSEL = 0x014e;
	public static final int CB_GETCURSEL = 0x0147;
	public static final int WM_CLOSE = 0x0010;
	public static final int WM_GETTEXTLENGTH = 0x000E;
	public static final int BM_CLICK = 0x00F5;
	public static final int WM_LBUTTONDOWN = 513;
	public static final int WM_LBUTTONUP = 514;
	public static final int MK_LBUTTON = 0x0001;
	public static final int CB_SELECTSTRING = 333;
	public static final int CBN_SELCHANGE = 0x0001;
	public static final int WM_COMMAND = 0x111;
	public static final int BM_SETCHECK = 0xf1;
	public static final int BST_CHECKED = 0x1;
	public static final int BM_GETCHECK = 0xf0;
	public static final int BST_INDETERMINATE = 0x2;
	public static final int BST_UNCHECKED = 0x0;

	static HWND TfrmCargoOrderOld;
	static HWND __TXiPanel_distanceAndPrice;
	static HWND ___TJvXPButton_closeBtn;

	static HWND ____TEdit10; // 상차지 시도
	static HWND ____TEdit4; // 상차지 시군
	static HWND ____TEdit2; // 상차지 동읍리

	static HWND ____TEdit8; // 하차지 시도
	static HWND ____TEdit3; // 하차지 시군
	static HWND ____TEdit1; // 하차지 동읍리

	static HWND ___TPanel5; // 정보입력창 전체
	static HWND ____TEdit9_startAddr; // 상차지
	static HWND ____TEdit7_endAddr; // 하차지
	static HWND ____TRzComboBox_ton; // 톤수
	static HWND ___TRzPanel;
	static HWND ____TDBGrid; // 상하차지 선택 리스트
	static HWND ___TBitBtn_newBtn; // 신규(F3) 버튼
	static HWND ___TBitBtn_registBtn; // 화물등록 버튼
	static HWND ____TRzComboBox_carsort; // 차종
	static HWND ____TComboBox_arrival; // 도착
	static HWND ____TwCombo_payment_method; // 지불방법 운송비구분
	static HWND ____TwNumEdit_shipping_fee; // 운송료
	static HWND ____TwNumEdit_commission; // 수수료
	static HWND ____TwNumEdit_total_price; // 합계
	static HWND ____TEdit5_more_infomation; // 추가정보
	static HWND ____TMemo; // 화물정보

	static HWND ____TCheckBox_mixup; // 혼적
	static HWND ____TEdit_load_capacity; // 적재 중량
	static HWND ___TBitBtn_search_btn; // 검색 버튼
	static HWND ___TEdit_search_text; // 검색 내용
	static HWND ____TEdit_search_option; // 검색 옵션 / 상하차지 등
	static HWND ____TCheckBox_reserved; // 예약

	String loadAddr;
	String alightAddr;
	static String title = "CapturePrice";
	static TCPServer tcp;

	static private final Map<Integer, Integer> mappingFileds = new HashMap<Integer, Integer>();

	public static void main(String[] args) {
		new CargoMain();
	}

	public void showAlert(String msg) {
		int type = JOptionPane.showOptionDialog(null, msg, "알림", JOptionPane.CLOSED_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (type == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
	}

	public interface User32Extra extends User32 {

		User32Extra INSTANCE = (User32Extra) Native.loadLibrary("user32", User32Extra.class,
				W32APIOptions.DEFAULT_OPTIONS);

		public HDC GetWindowDC(HWND hWnd);

		public boolean GetClientRect(HWND hWnd, RECT rect);

	}

	public interface GDI32Extra extends GDI32 {

		GDI32Extra INSTANCE = (GDI32Extra) Native.loadLibrary("gdi32", GDI32Extra.class, W32APIOptions.DEFAULT_OPTIONS);

		public boolean BitBlt(HDC hObject, int nXDest, int nYDest, int nWidth, int nHeight, HDC hObjectSource,
				int nXSrc, int nYSrc, DWORD dwRop);

	}

	public interface WinGDIExtra extends WinGDI {
		public DWORD SRCCOPY = new DWORD(0x00CC0020);
	}

	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

		boolean ShowWindow(HWND hWnd, int nCmdShow);

		HWND FindWindow(String lpClassName, String lpWindowName);

		int SendMessage(HWND ____TRzComboBox_ton, int cB_SETCURSEL, int ton_idx, int i);

		int GetWindowRect(HWND handle, int[] rect);

		int SendMessage(HWND hWnd, int msg, int wParam, String lParam);

		HWND FindWindowEx(HWND parent, HWND child, String className, String window);

		HWND FindWindowExA(HWND hwndParent, HWND childAfter, String className, String windowName);

		HWND FindWindowA(String className, String windowName);

		HWND PostMessage(HWND hWnd, int msg, int wParam, int lParam);

		HDC GetDC(HWND hWnd);

		void ReleaseDC(HWND hWnd, HDC hdcWindow);

		void SendMessageA(HWND ____TEdit10, int wM_GETTEXT, int i, byte[] loadAddr1);

		void SendMessage(HWND ____TRzComboBox_ton, int cB_GETCURSEL, int ton_idx, char[] buf);

		void SendMessage(HWND ____TRzComboBox_ton, int cB_SELECTSTRING, String string, int i);

		int GetWindowLongPtr(HWND hWnd, int nIndex);

		void SendMessage(HWND tonParent, int cB_SETCURSEL, int send_cbn_selchange, HWND ____TRzComboBox_ton);

		void SendMessage(int tonParent, int wM_COMMAND, int send_cbn_selchange, HWND ____TRzComboBox_ton);

		void GetWindowThreadProcessId(HWND capturePrice, DWORD dwProcessId);

		void PostMessage(HWND ____TwNumEdit_shipping_fee, int wM_CHAR, int wParam, String string);

		void PostMessage(HWND ____TwNumEdit_shipping_fee, int wM_CHAR, String string, int lParam);

		int GetWindowText(HWND tMessageForm, char[] title, int length);

		boolean SendMessageTimeout(HWND ___TBitBtn_registBtn, int bmClick, int i, int j, int fuFlags, int uTimeout,
				int lpdwResult);
	}

	public static void checkMultipleWindow() {
		HWND cp = User32.INSTANCE.FindWindow(null, title);
		User32.INSTANCE.PostMessage(cp, 18, 0, 0);
	}

	private void getHwnd() {
		HWND TfrmCargoMain = User32.INSTANCE.FindWindow("TfrmCargoMain", null);
		if (TfrmCargoMain == null) {
			showAlert("24시콜화물 프로그램이 실행되어있어야 합니다.");
		}
		User32.INSTANCE.ShowWindow(TfrmCargoMain, 1);

		HWND MDIClient = User32.INSTANCE.FindWindowEx(TfrmCargoMain, null, "MDIClient", null);

		TfrmCargoOrderOld = User32.INSTANCE.FindWindowEx(MDIClient, null, "TfrmCargoOrderOld", null);
		HWND _TPanel1 = User32.INSTANCE.FindWindowEx(TfrmCargoOrderOld, null, "TPanel", null);
		HWND _TPanel2 = User32.INSTANCE.FindWindowEx(TfrmCargoOrderOld, _TPanel1, "TPanel", null);
		HWND __TPanel3 = User32.INSTANCE.FindWindowEx(_TPanel2, null, "TPanel", null);

		HWND __TXiPanel1 = User32.INSTANCE.FindWindowEx(_TPanel2, null, "TXiPanel", null);
		___TBitBtn_search_btn = User32.INSTANCE.FindWindowEx(__TXiPanel1, null, null, "검색");

		___TEdit_search_text = User32.INSTANCE.FindWindowEx(__TXiPanel1, ___TBitBtn_search_btn, "TEdit", null);
		HWND ___TComboBox_search_option = User32.INSTANCE.FindWindowEx(__TXiPanel1, ___TEdit_search_text, "TComboBox",
				null);
		____TEdit_search_option = User32.INSTANCE.FindWindowEx(___TComboBox_search_option, null, "Edit", null);

		HWND __TXiPanel2 = User32.INSTANCE.FindWindowEx(_TPanel2, __TXiPanel1, "TXiPanel", null);
		___TBitBtn_newBtn = User32.INSTANCE.FindWindowEx(__TXiPanel2, null, null, "신규(F3)");

		___TBitBtn_registBtn = User32.INSTANCE.FindWindowEx(__TXiPanel2, null, null, "화물등록");

		HWND __TPanel4 = User32.INSTANCE.FindWindowEx(_TPanel2, __TPanel3, "TPanel", null);

		HWND ___TPanel4_1 = User32.INSTANCE.FindWindowEx(__TPanel4, null, "TPanel", null);

		HWND ____TXiPanel = User32.INSTANCE.FindWindowEx(___TPanel4_1, null, null, "상차방법");
		HWND ____TPanel1 = User32.INSTANCE.FindWindowEx(___TPanel4_1, ____TXiPanel, "TPanel", null);
		____TCheckBox_reserved = User32.INSTANCE.FindWindowEx(____TPanel1, null, null, "예약");
		//
		
		HWND ____TPanel2 = User32.INSTANCE.FindWindowEx(___TPanel4_1, ____TPanel1, "TPanel", null);
		____TCheckBox_mixup = User32.INSTANCE.FindWindowEx(____TPanel2, null, null, "혼적");

		___TPanel5 = User32.INSTANCE.FindWindowEx(__TPanel4, ___TPanel4_1, "TPanel", null);
		___TRzPanel = User32.INSTANCE.FindWindowEx(___TPanel5, null, "TRzPanel", null);

		____TDBGrid = User32.INSTANCE.FindWindowEx(___TRzPanel, null, "TDBGrid", null);

		____TEdit1 = User32.INSTANCE.FindWindowEx(___TPanel5, null, "TEdit", null); // 하차지 동읍리
		____TEdit2 = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit1, "TEdit", null); // 상차지 동읍리
		____TEdit3 = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit2, "TEdit", null); // 하차지 시군
		____TEdit4 = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit3, "TEdit", null); // 상차지 시군
		____TEdit5_more_infomation = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit4, "TEdit", null); // 추가 정보
		HWND ____TEdit6 = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit5_more_infomation, "TEdit", null);

		____TEdit7_endAddr = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit6, "TEdit", null);
		____TEdit8 = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit7_endAddr, "TEdit", null); // 상차지 시
		____TEdit9_startAddr = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit8, "TEdit", null);
		____TEdit10 = User32.INSTANCE.FindWindowEx(___TPanel5, ____TEdit9_startAddr, "TEdit", null); // 하차지 시

		____TRzComboBox_carsort = User32.INSTANCE.FindWindowEx(___TPanel5, null, "TRzComboBox", null);
		____TRzComboBox_ton = User32.INSTANCE.FindWindowEx(___TPanel5, ____TRzComboBox_carsort, "TRzComboBox", null);
		____TComboBox_arrival = User32.INSTANCE.FindWindowEx(___TPanel5, null, "TComboBox", null);
		____TwCombo_payment_method = User32.INSTANCE.FindWindowEx(___TPanel5, null, "TwCombo", null);

		HWND ____TXiPenal_load_capacity = User32.INSTANCE.FindWindowEx(___TPanel5, ____TwCombo_payment_method,
				"TXiPanel", null);
		____TEdit_load_capacity = User32.INSTANCE.FindWindowEx(____TXiPenal_load_capacity, null, "TEdit", null);

		____TwNumEdit_shipping_fee = User32.INSTANCE.FindWindowEx(___TPanel5, null, "TwNumEdit", null);
		____TwNumEdit_total_price = User32.INSTANCE.FindWindowEx(___TPanel5, ____TwNumEdit_shipping_fee, "TwNumEdit",
				null);
		____TwNumEdit_commission = User32.INSTANCE.FindWindowEx(___TPanel5, ____TwNumEdit_total_price, "TwNumEdit",
				null);
		____TMemo = User32.INSTANCE.FindWindowEx(___TPanel5, null, "TMemo", null);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static ExecutorService executorService = Executors.newCachedThreadPool();
	static Socket socket;
	int port = 8000;

	public CargoMain() {
		this.setTitle(title + "2209211406");
		int frameH = 100;
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(500, 400);

		setLayout(new FlowLayout());
		setResizable(false);
		setBounds(ss.width / 2 - frameSize.width / 2, 0, frameSize.width, frameSize.height);

		JTextArea textArea = new JTextArea(20, 40);
		textArea.setSize(frameSize);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);

		add(scrollPane);
		setVisible(true);
		getHwnd();

		executorService.submit(() -> {
			BufferedReader br = null;
			BufferedWriter bw = null;
			int BUF_SIZE = 1024 * 7;

			textArea.append("서버 시작..\n");
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				try {
					loadAddr = null;
					alightAddr = null;
					textArea.append("접속 대기..\n");
					socket = serverSocket.accept();
					socket.setSoTimeout(5000);

					String cip = socket.getInetAddress().toString();
					textArea.append("[ " + cip + " ] 접속\n");
					if (!cip.contains("112.175.243.19") && !cip.contains("192.168.0.1")) {
						textArea.append("[ " + cip + " ] 허용ip 아님\n");
						socket.close();
						continue;
					}

					br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf8"), BUF_SIZE);
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

					String params = br.readLine();
					textArea.append(params + "\n");

					InfoModel info = new InfoModel(params);
					// info.price = 10000;
					if (mappingFileds.get(info.time) != null) {
						// textArea.append("완료 중복 시간");
						// socket.close();
						// continue;
					}
					mappingFileds.put(info.time, 0);
					setAddr(info);
					loadAddr = getLoadAddrText();
					alightAddr = getAlightAddrText();
					setTonCar(info);

					JSONObject res = new JSONObject();
					res.put("req_load_addr", info.load_addr);
					res.put("req_alight_addr", info.alight_addr);
					res.put("res_load_addr", loadAddr);
					res.put("res_alight_addr", alightAddr);
					res.put("capture_image", "");
					res.put("proc", 1);

					if (info.type.contains("regist")) {
						setRegistOption(info);
						saveBmp(info.zin_36);
						registBtnClick();
						String confirmMsg = confirmMessageForm();
						String msg = "";

						if (confirmMsg == null) {
							msg += "등록실패";
							res.put("proc", 0);
						} else {								 
							if (!confirmMsg.contains("접수")) {
								res.put("proc", 0);
							}
							msg += confirmMsg;
						}

//						if (registVerification(info)) {
//							msg += " / 검증(" + info.zin_36 + ")목록확인 존재o, 상태확인 필요)";
//						}
						
						String memo = getMemo();
						
						String log = "등록\n[params]" + params + "\n" 
								+ "[msg]" + msg + "\n"
								+ "[memo]" + memo + "\n"
								+ "[res]" + res.toJSONString();
						
						writeLog(info.zin_36, log);
						

						res.put("msg", msg.replace("\n", ""));
						textArea.append("[ " + cip + " ] " + res.toJSONString() + "\n");
					} else {
						if (loadAddr.replace(" ", "").length() == 0 || alightAddr.replace(" ", "").length() == 0) {
							throw new Exception("상하차지 정보가 잘못 들어감");
						}

						final BufferedImage priceImg = getDistancePrice();
						String ocr = OCR(priceImg);
						String arr = Arrays.toString(ocr.split(":"));

						if (arr.length() < 3) {
							throw new Exception("요금정보 불러오기 실패");
						}
						System.out.println("###########" + ocr);
						String price = ocr.split(":")[2].replace("[^\\d.]", "").replace("?", "7");
						String imageBase64 = imgToBase64String(priceImg, "png");
						res.put("ocr", ocr.replace("\n", "").trim());
						res.put("price", price);
						
						String log = "가격조회\n[params]" + params + "\n" 
									+ "[res]" + res.toJSONString();
						
						String filename = info.load_addr + " 에서 " + info.alight_addr;
						saveBmp(filename);
						writeLog(filename, log);
						
						textArea.append("[ " + cip + " ] " + price + "\n");
					}
					bw.write(res.toJSONString());
					bw.flush();
					setResizable(false);
					setVisible(true);

				} catch (Exception ex) {
					writeLog("error = ", ex.getMessage());
					textArea.append("\n Server exception: " + ex.getMessage());
					JSONObject res = new JSONObject();
					res.put("msg", ex.getMessage());
					res.put("proc", 0);
					bw.write(res.toJSONString());
					bw.flush();
					if (socket != null && socket.isConnected()) {
						socket.close();	
					}
					continue;
				}
			}
		});
	}

	public void setAddr(InfoModel info) {
		try {
			closeSearchAddrWindow();
			User32.INSTANCE.SendMessage(___TBitBtn_newBtn, (int) BM_CLICK, 0, 0); // 신규(F3) 누르기
			Thread.sleep(waitTime);

			User32.INSTANCE.SendMessage(____TEdit9_startAddr, WM_SETFOCUS, 0, 0); // 상차지 포커스
			Thread.sleep(waitTime);
			User32.INSTANCE.SendMessage(____TEdit9_startAddr, WM_SETTEXT, 0, info.load_addr); // 상차지 텍스트 넣기
			Thread.sleep(waitTime);
			User32.INSTANCE.PostMessage(____TEdit9_startAddr, WM_KEYDOWN, VK_RETURN, 1); // 상차지 엔터
			Thread.sleep(waitTime);
			closeSearchAddrWindow();
			Thread.sleep(waitTime);
			User32.INSTANCE.PostMessage(____TDBGrid, WM_KEYDOWN, VK_RETURN, 0); // 상차지 리스트 엔터
			Thread.sleep(waitTime);
			User32.INSTANCE.SendMessage(____TEdit9_startAddr, WM_KILLFOCUS, 0, 0); // 상차지 포커스 해제
			Thread.sleep(waitTime);

			User32.INSTANCE.SendMessage(____TEdit7_endAddr, WM_SETFOCUS, 0, 0);
			Thread.sleep(waitTime);
			User32.INSTANCE.SendMessage(____TEdit7_endAddr, WM_SETTEXT, 0, info.alight_addr);
			Thread.sleep(waitTime);
			User32.INSTANCE.PostMessage(____TEdit7_endAddr, WM_KEYDOWN, VK_RETURN, 1);
			Thread.sleep(waitTime);
			closeSearchAddrWindow();
			Thread.sleep(waitTime);
			User32.INSTANCE.PostMessage(____TDBGrid, WM_KEYDOWN, VK_RETURN, 0);
			Thread.sleep(waitTime);
			User32.INSTANCE.SendMessage(____TEdit7_endAddr, WM_KILLFOCUS, 0, 0);
			Thread.sleep(waitTime);
			closeSearchAddrWindow();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int waitTime = 100;

	public void setTonCar(InfoModel info) {

		try {
			Thread.sleep(waitTime);
			int leftFrameParent = User32.INSTANCE.GetWindowLongPtr(____TRzComboBox_ton, -12);
			int send_cbn_selchange = MakeWParam(leftFrameParent, CBN_SELCHANGE);

			// 톤수 설정
			User32.INSTANCE.PostMessage(____TRzComboBox_ton, CB_SETCURSEL, info.ton_idx, 0);
			User32.INSTANCE.SendMessage(leftFrameParent, WM_COMMAND, send_cbn_selchange, ____TRzComboBox_ton);
			Thread.sleep(waitTime);
			// 차종 설정
			User32.INSTANCE.PostMessage(____TRzComboBox_carsort, CB_SETCURSEL, info.car_sort_idx, 0);
			Thread.sleep(waitTime);
			User32.INSTANCE.SendMessage(leftFrameParent, WM_COMMAND, send_cbn_selchange, ____TRzComboBox_carsort);
			// =============================================================== search price
			// 는 여기까지만 필요
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setRegistOption(InfoModel info) {

		try {
			int leftFrameParent = User32.INSTANCE.GetWindowLongPtr(____TRzComboBox_ton, -12);
			int send_cbn_selchange = MakeWParam(leftFrameParent, CBN_SELCHANGE);

			Thread.sleep(waitTime);

			// 도착 설정
			User32.INSTANCE.PostMessage(____TComboBox_arrival, CB_SETCURSEL, info.arrival_idx, 0);
			Thread.sleep(waitTime);
			User32.INSTANCE.SendMessage(leftFrameParent, WM_COMMAND, send_cbn_selchange, ____TComboBox_arrival);
			Thread.sleep(waitTime);
			// 지불 방식 운송비구분
			sendChar(____TwCombo_payment_method, info.payment_method);
			Thread.sleep(waitTime);
			// 운송료
			sendChar(____TwNumEdit_shipping_fee, info.price + "");
			Thread.sleep(waitTime);
			// 수수료
			sendChar(____TwNumEdit_commission, info.commission + "");
			Thread.sleep(waitTime);
			// 화물 정보
			sendChar(____TEdit5_more_infomation, "");
			sendChar(____TEdit5_more_infomation, info.freight_info);
			Thread.sleep(waitTime);
			User32.INSTANCE.SendMessage(____TMemo, WM_SETFOCUS, 0, 0);
			Thread.sleep(waitTime);

			sendChar(____TEdit_load_capacity, info.ton_info);

			if (info.mixed_loading.contains("혼적")) {
				if (User32.INSTANCE.SendMessage(____TCheckBox_mixup, BM_GETCHECK, 0, 0) == BST_UNCHECKED) {
					User32.INSTANCE.SendMessage(____TCheckBox_mixup, (int) BM_CLICK, 0, 0);
				}
			} else {
				if (User32.INSTANCE.SendMessage(____TCheckBox_mixup, BM_GETCHECK, 0, 0) == BST_CHECKED) {
					User32.INSTANCE.SendMessage(____TCheckBox_mixup, (int) BM_CLICK, 0, 0);
				}
			}
			
			if (info.reserved.contains("예약")) {
				if (User32.INSTANCE.SendMessage(____TCheckBox_reserved, BM_GETCHECK, 0, 0) == BST_UNCHECKED) {
					User32.INSTANCE.SendMessage(____TCheckBox_reserved, (int) BM_CLICK, 0, 0);
				}
			} else {
				if (User32.INSTANCE.SendMessage(____TCheckBox_reserved, BM_GETCHECK, 0, 0) == BST_CHECKED) {
					User32.INSTANCE.SendMessage(____TCheckBox_reserved, (int) BM_CLICK, 0, 0);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void registBtnClick() {
		User32.INSTANCE.SendMessageTimeout(___TBitBtn_registBtn, (int) BM_CLICK, 0, 0, 0, 1, 0); // 등록
	}

	public String confirmMessageForm() {
		try {
			Thread.sleep(500);
			HWND TMessageForm = User32.INSTANCE.FindWindow("TMessageForm", null);
			if (TMessageForm != null) {
				HWND TButton = User32.INSTANCE.FindWindowEx(TMessageForm, null, "TButton", null);
				final char[] title = new char[100];
				User32.INSTANCE.GetWindowText(TMessageForm, title, title.length);
				String caption = Native.toString(title);
				caption += OCR(capture(TMessageForm, 1));

				User32.INSTANCE.SendMessage(TButton, (int) BM_CLICK, 0, 0);
				User32.INSTANCE.SendMessage(TButton, (int) BM_CLICK, 0, 0);
				return caption;
			}

			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void saveBmp(String filename) {
        try {
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HH_mm_ss");
    		String now = dtf.format(LocalDateTime.now());
    		
    		BufferedImage image = capture(___TPanel5, 1);
    		File file = new File("C:\\Users\\user\\Desktop\\log\\" + (filename.length() == 0 || filename == null ? now : filename) + ".jpg");
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void writeLog(String filename, String msg) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HH_mm_ss");
		String now = dtf.format(LocalDateTime.now());
		File file = new File("C:\\Users\\user\\Desktop\\log\\" + (filename.length() == 0 || filename == null ? now : filename) + ".txt");
		
		now += "\n" + msg;
		String str = now;

		try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		    writer.write(str);
		    writer.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public String getMemo() {
		int len = User32.INSTANCE.SendMessage(____TMemo, WM_GETTEXTLENGTH, 0, 0);
		char[] memo = new char[len];
		User32.INSTANCE.SendMessage(____TMemo, WM_GETTEXT, 512, memo);
		String memoStr = Native.toString(memo).replace("\\p{Sc}", "");
		return memoStr;
	}

	public boolean registVerification(InfoModel info) {
		try {
			System.out.println("registVerification start");
			Thread.sleep(100);
			sendChar(____TEdit_search_option, "하차지");
			Thread.sleep(100);
			sendChar(___TEdit_search_text, alightAddr);
			Thread.sleep(500);

			User32.INSTANCE.SendMessageTimeout(___TBitBtn_search_btn, (int) BM_CLICK, 0, 0, 0, 1, 0);
			Thread.sleep(300);
			User32.INSTANCE.SendMessageTimeout(___TBitBtn_search_btn, (int) BM_CLICK, 0, 0, 0, 1, 0);
			Thread.sleep(300);
			// ____TMemo
			
			int len = User32.INSTANCE.SendMessage(____TMemo, WM_GETTEXTLENGTH, 0, 0);
			char[] memo = new char[len];
			User32.INSTANCE.SendMessage(____TMemo, WM_GETTEXT, 512, memo);
			String memoStr = Native.toString(memo).replace("\\p{Sc}", "");
			if (memoStr.contains(info.zin_36)) {
				return true;
			} else {
				return false;
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void btnClick(HWND hwnd) {
		User32.INSTANCE.PostMessage(hwnd, WM_LBUTTONDOWN, MK_LBUTTON, 1);
		User32.INSTANCE.PostMessage(hwnd, WM_LBUTTONUP, MK_LBUTTON, 1);
	}

	public void sendChar(HWND hwnd, String str) {
		User32.INSTANCE.SendMessage(hwnd, WM_SETFOCUS, 0, 0);
		for (int i = 0; i < str.length(); i++) {
			User32.INSTANCE.SendMessage(hwnd, WM_CHAR, str.charAt(i), 0);
		}
		User32.INSTANCE.SendMessage(hwnd, WM_KEYDOWN, VK_RETURN, 0);
		User32.INSTANCE.SendMessage(hwnd, WM_KILLFOCUS, 0, 0);
	}

	static int MakeWParam(int loWord, int hiWord) {
		return (loWord & 0xFFFF) + ((hiWord & 0xFFFF) << 16);
	}

	public String getLoadAddrText() {
		try {
			int len = User32.INSTANCE.SendMessage(____TEdit10, WM_GETTEXTLENGTH, 0, 0);
			char[] loadAddr1 = new char[len];
			User32.INSTANCE.SendMessage(____TEdit10, WM_GETTEXT, 512, loadAddr1);
			
			len = User32.INSTANCE.SendMessage(____TEdit4, WM_GETTEXTLENGTH, 0, 0);
			char[] loadAddr2 = new char[len];
			User32.INSTANCE.SendMessage(____TEdit4, WM_GETTEXT, 512, loadAddr2);
			
			len = User32.INSTANCE.SendMessage(____TEdit2, WM_GETTEXTLENGTH, 0, 0);
			char[] loadAddr3 = new char[len];
			User32.INSTANCE.SendMessage(____TEdit2, WM_GETTEXT, 512, loadAddr3);

			String loadAddr = Native.toString(loadAddr1) + " " + Native.toString(loadAddr2) + " "
					+ Native.toString(loadAddr3);
			System.out.println(loadAddr);
			return loadAddr;
		} catch (Exception e) {
			return "";
		}
	}

	public String getAlightAddrText() {
		try {
			int len = User32.INSTANCE.SendMessage(____TEdit8, WM_GETTEXTLENGTH, 0, 0);
			char[] alightAddr1 = new char[len];
			User32.INSTANCE.SendMessage(____TEdit8, WM_GETTEXT, 100, alightAddr1);
			
			len = User32.INSTANCE.SendMessage(____TEdit3, WM_GETTEXTLENGTH, 0, 0);
			char[] alightAddr2 = new char[len];
			User32.INSTANCE.SendMessage(____TEdit3, WM_GETTEXT, 100, alightAddr2);
			
			len = User32.INSTANCE.SendMessage(____TEdit1, WM_GETTEXTLENGTH, 0, 0);
			char[] alightAddr3 = new char[len];
			User32.INSTANCE.SendMessage(____TEdit1, WM_GETTEXT, 100, alightAddr3);

			String alightAddr = Native.toString(alightAddr1) + " " + Native.toString(alightAddr2) + " "
					+ Native.toString(alightAddr3);
			System.out.println(alightAddr);
			return alightAddr;
		} catch (Exception e) {
			return "";
		}
	}

	public void closeSearchAddrWindow() {
		try {
			HWND TfrmAddrSearchXP = User32.INSTANCE.FindWindow("TfrmAddrSearchXP", null);
			HWND _TRzPanel = User32.INSTANCE.FindWindowEx(TfrmAddrSearchXP, null, "TRzPanel", null);
			HWND __TRzPageControl = User32.INSTANCE.FindWindowEx(_TRzPanel, null, "TRzPageControl", null);
			HWND ___TRzTabSheet = User32.INSTANCE.FindWindowEx(__TRzPageControl, null, "TRzTabSheet", null);
			HWND ____TPanel = User32.INSTANCE.FindWindowEx(___TRzTabSheet, null, "TPanel", null);
			HWND _____TRealGrid = User32.INSTANCE.FindWindowEx(____TPanel, null, "TRealGrid", null);
			
			HWND _TRzPanel2 = User32.INSTANCE.FindWindowEx(TfrmAddrSearchXP, _TRzPanel, "TRzPanel", null);
			HWND __TPanel = User32.INSTANCE.FindWindowEx(_TRzPanel2, null, "TRzPanel", null);
			HWND ___TJvXPButton = User32.INSTANCE.FindWindowEx(__TPanel, null, "__TJvXPButton", null);

			User32.INSTANCE.PostMessage(_____TRealGrid, 0x0203, MK_LBUTTON, MakeWParam(50, 50));
			User32.INSTANCE.PostMessage(_____TRealGrid, WM_LBUTTONUP, MK_LBUTTON, MakeWParam(50, 50));
			Thread.sleep(300);
			
//			btnClick(___TJvXPButton);
			//User32.INSTANCE.PostMessage(TfrmAddrSearchXP, WM_CLOSE, 0, 0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String OCR(BufferedImage image) {
		try {

			ITesseract tesseract = new Tesseract();
			Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
			Path filePath = Paths.get(root.toString(), "tessdata");
			tesseract.setDatapath(filePath.toString());
			tesseract.setLanguage("kor+eng");
			String result = tesseract.doOCR(image);
			return result;
		} catch (TesseractException e) {
			JOptionPane.showMessageDialog(null, "err ##" + e.getMessage());
			return null;
		}
	}

	private static BufferedImage getDistancePrice() {
		if (__TXiPanel_distanceAndPrice == null) {
			HWND _TRzPanel = User32.INSTANCE.FindWindowEx(TfrmCargoOrderOld, null, "TRzPanel", null);
			HWND _TRzPanel2 = User32.INSTANCE.FindWindowEx(TfrmCargoOrderOld, _TRzPanel, "TRzPanel", null);
			__TXiPanel_distanceAndPrice = User32.INSTANCE.FindWindowEx(_TRzPanel2, null, "TXiPanel", null);
		}

		return capture(__TXiPanel_distanceAndPrice, 1);
	}

	private static BufferedImage getPictureInfoPanel() {
		return capture(___TPanel5, 1);
	}

	public static BufferedImage capture(HWND hWnd, int multiply) {

		HDC hdcWindow = User32.INSTANCE.GetDC(hWnd);
		HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);

		RECT bounds = new RECT();
		User32Extra.INSTANCE.GetClientRect(hWnd, bounds);

		int width = bounds.right - bounds.left;
		int height = bounds.bottom - bounds.top;

		HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, width, height);

		HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
		GDI32Extra.INSTANCE.BitBlt(hdcMemDC, 0, 0, width, height, hdcWindow, 0, 0, WinGDIExtra.SRCCOPY);

		GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
		GDI32.INSTANCE.DeleteDC(hdcMemDC);

		BITMAPINFO bmi = new BITMAPINFO();
		bmi.bmiHeader.biWidth = width;
		bmi.bmiHeader.biHeight = -height;
		bmi.bmiHeader.biPlanes = 1;
		bmi.bmiHeader.biBitCount = 32;
		bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

		Memory buffer = new Memory(width * height * 4);
		GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, buffer.getIntArray(0, width * height), 0, width);

		GDI32.INSTANCE.DeleteObject(hBitmap);
		User32.INSTANCE.ReleaseDC(hWnd, hdcWindow);

		return resize(image, width * multiply, height * multiply);

	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	private static final String KILL = "taskkill /f /pid ";

	public static void killProcess(int pid) throws Exception {

		Runtime.getRuntime().exec(KILL + pid);

	}

	public static String imgToBase64String(final RenderedImage img, final String formatName) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try (final OutputStream b64os = Base64.getEncoder().wrap(os)) {
			ImageIO.write(img, formatName, b64os);
		} catch (final IOException ioe) {
			// throw new UncheckedIOException(ioe);
		}
		return os.toString();
	}
}
