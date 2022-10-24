using System.Drawing.Imaging;
using System.Runtime.InteropServices;
using System.Text;
using Tesseract;
using ImageFormat = System.Drawing.Imaging.ImageFormat;

namespace DistancePriceCaptureOCR
{
    class FindProcess
    {
        Form1 form;
        public FindProcess(Form1 form)
        {
            this.form = form;
            getHWND();
        }

        private const int SW_SHOWNORMAL = 1;
        private const int SW_SHOWMINIMIZED = 2;
        private const int SW_SHOWMAXIMIZED = 3;

        [DllImport("user32.dll")]
        private static extern bool SetForegroundWindow(int hWnd); [DllImport("user32.dll")]
        private static extern bool ShowWindowAsync(int hWnd, int nCmdShow);

        [DllImport("user32.dll")]
        public static extern int FindWindowEx(int hWnd1, int hWnd2, string lpsz1, string? lpsz2);

        //[DllImport("user32.dll")]
        //public static extern IntPtr FindWindow(string lpClassName, string lpWindowName);

        [DllImport("user32.dll")]
        public static extern int FindWindow(string lpClassName, string? lpWindowName);

        [DllImport("user32.dll", CharSet = CharSet.Unicode, SetLastError = true)]
        public static extern bool PostMessage(int hWnd, int msg, int wParam, int lParam);

        //[DllImport("user32.dll")]
        //public static extern int SendMessage(IntPtr hWnd, int msg, int wParam, IntPtr lParam);

        [DllImport("user32.dll")]
        static extern int GetDlgItem(int hWnd, int nIDDlgItem);

        [DllImport("user32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        static extern bool PrintWindow(int hwnd, int hDC, int nFlags);

        [DllImport("user32.dll", SetLastError = true)]
        static extern int GetWindowRgn(int hWnd, int hRgn);

        [DllImport("gdi32.dll")]
        static extern int CreateRectRgn(int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);

        [DllImport("User32.dll")]
        static extern bool SetWindowText(int hWnd, string lParam);

        [DllImport("User32.dll")]
        static extern bool ShowWindow(int hWnd, int nCmdShow);
        
        [DllImport("User32.dll")]
        static extern int GetWindowLongPtr(int hWnd, int nIndex);

        [System.Runtime.InteropServices.DllImport("user32.dll", EntryPoint = "SendMessage", CharSet = System.Runtime.InteropServices.CharSet.Auto)]
        public static extern bool SendMessage(int hWnd, int Msg, int wParam, StringBuilder lParam);

        [System.Runtime.InteropServices.DllImport("user32.dll", SetLastError = true)]
        public static extern int SendMessage(int hWnd, int Msg, int wparam, int lparam);

        [System.Runtime.InteropServices.DllImport("user32.dll", SetLastError = true)]
        public static extern int SendMessage(int hWnd, int uMsg, int wParam, string lParam);
        [DllImport("user32", SetLastError = true, CharSet = CharSet.Auto)]
        private static extern IntPtr SendMessageTimeout
(
    int windowHandle,
    int message,
    int wordParameter,
    int longParameter,
    SendMessageTimeoutFlag flag,
    int timeout
);

        [Flags]
        public enum SendMessageTimeoutFlag : uint
        {
            /// <summary>
            /// SMTO_NORMAL
            /// </summary>
            SMTO_NORMAL = 0x0,

            /// <summary>
            /// SMTO_BLOCK
            /// </summary>
            SMTO_BLOCK = 0x1,

            /// <summary>
            /// SMTO_ABORTIFHUNG
            /// </summary>
            SMTO_ABORTIFHUNG = 0x2,

            /// <summary>
            /// SMTO_NOTIMEOUTIFNOTHUNG
            /// </summary>
            SMTO_NOTIMEOUTIFNOTHUNG = 0x8,

            /// <summary>
            /// SMTO_ERRORONEXIT
            /// </summary>
            SMTO_ERRORONEXIT = 0x20
        }

       int EM_REPLACESEL = 0x00c2;
       int EM_SETMODIFY = 0x00b9;
       int WM_SETTEXT = 0x000c;
       int WM_GETTEXT = 0x000D;
       int EM_SETSEL = 0x00b1;
       int WM_KEYDOWN = 0x100;
       int WM_KEYUP = 0x101;
       int WM_CHAR = 0x0102;
       int VK_RETURN = 0x0D;
       int VK_ENTER = 0x0D;
       int VK_TAB = 0x09;
       int WM_SETFOCUS = 0x0007;
       int WM_KILLFOCUS = 0x0008;
       int CB_SETCURSEL = 0x014e;
       int CB_GETCURSEL = 0x0147;
       int WM_CLOSE = 0x0010;
       int WM_GETTEXTLENGTH = 0x000E;
       int BM_CLICK = 0x00F5;
       int WM_LBUTTONDOWN = 513;
       int WM_LBUTTONUP = 514;
       int MK_LBUTTON = 0x0001;
       int CB_SELECTSTRING = 333;
       int CBN_SELCHANGE = 0x0001;
       int WM_COMMAND = 0x111;
       int BM_SETCHECK = 0xf1;
       int BST_CHECKED = 0x1;
       int BM_GETCHECK = 0xf0;
       int BST_INDETERMINATE = 0x2;
       int BST_UNCHECKED = 0x0;

        int __TXiPanel_distanceAndPrice = 0;
        int ___TJvXPButton_closeBtn = 0;

        int TfrmCargoOrderOld;

        int ____TEdit10; // 상차지 시도
        int ____TEdit4; // 상차지 시군
        int ____TEdit2; // 상차지 동읍리

        int ____TEdit8; // 하차지 시도
        int ____TEdit3; // 하차지 시군
        int ____TEdit1; // 하차지 동읍리

        int ___TPanel5; // 정보입력창 전체
        int ____TEdit9_startAddr; // 상차지
        int ____TEdit7_endAddr; // 하차지
        int ____TRzComboBox_ton; // 톤수
        int ___TRzPanel;
        int ____TDBGrid; // 상하차지 선택 리스트
        int ___TBitBtn_newBtn; // 신규(F3) 버튼
        int ___TBitBtn_registBtn; // 화물등록 버튼
        int ____TRzComboBox_carsort; // 차종
        int ____TComboBox_arrival; // 도착
        int ____TwCombo_payment_method; // 지불방법 운송비구분
        int ____TwNumEdit_shipping_fee; // 운송료
        int ____TwNumEdit_commission; // 수수료
        int ____TwNumEdit_total_price; // 합계
        int ____TEdit5_more_infomation; // 추가정보
        int ____TMemo; // 화물정보

        int ____TCheckBox_mixup; // 혼적
        int ____TEdit_load_capacity; // 적재 중량
        int ___TBitBtn_search_btn; // 검색 버튼
        int ___TEdit_search_text; // 검색 내용
        int ____TEdit_search_option; // 검색 옵션 / 상하차지 등
        int ____TCheckBox_reserved; // 예약

        private void getHWND()
        {
            var TfrmCargoMain = FindWindow("TfrmCargoMain", null);
            ShowWindow(TfrmCargoMain, 1);

            var MDIClient = FindWindowEx(TfrmCargoMain, 0, "MDIClient", null);

            TfrmCargoOrderOld = FindWindowEx(MDIClient, 0, "TfrmCargoOrderOld", null);
            var _TPanel1 = FindWindowEx(TfrmCargoOrderOld, 0, "TPanel", null);
            var _TPanel2 = FindWindowEx(TfrmCargoOrderOld, _TPanel1, "TPanel", null);
            var __TPanel3 = FindWindowEx(_TPanel2, 0, "TPanel", null);

            var __TXiPanel1 = FindWindowEx(_TPanel2, 0, "TXiPanel", null);
            ___TBitBtn_search_btn = FindWindowEx(__TXiPanel1, 0, null, "검색");

            ___TEdit_search_text = FindWindowEx(__TXiPanel1, ___TBitBtn_search_btn, "TEdit", null);
            var ___TComboBox_search_option = FindWindowEx(__TXiPanel1, ___TEdit_search_text, "TComboBox",
                    null);
            ____TEdit_search_option = FindWindowEx(___TComboBox_search_option, 0, "Edit", null);

            var __TXiPanel2 = FindWindowEx(_TPanel2, __TXiPanel1, "TXiPanel", null);
            ___TBitBtn_newBtn = FindWindowEx(__TXiPanel2, 0, null, "신규(F3)");

            ___TBitBtn_registBtn = FindWindowEx(__TXiPanel2, 0, null, "화물등록");

            var __TPanel4 = FindWindowEx(_TPanel2, __TPanel3, "TPanel", null);

            var ___TPanel4_1 = FindWindowEx(__TPanel4, 0, "TPanel", null);

            var ____TXiPanel = FindWindowEx(___TPanel4_1, 0, null, "상차방법");
            var ____TPanel1 = FindWindowEx(___TPanel4_1, ____TXiPanel, "TPanel", null);
            ____TCheckBox_reserved = FindWindowEx(____TPanel1, 0, null, "예약");
            //

            var ____TPanel2 = FindWindowEx(___TPanel4_1, ____TPanel1, "TPanel", null);
            ____TCheckBox_mixup = FindWindowEx(____TPanel2, 0, null, "혼적");

            ___TPanel5 = FindWindowEx(__TPanel4, ___TPanel4_1, "TPanel", null);
            ___TRzPanel = FindWindowEx(___TPanel5, 0, "TRzPanel", null);

            ____TDBGrid = FindWindowEx(___TRzPanel, 0, "TDBGrid", null);

            ____TEdit1 = FindWindowEx(___TPanel5, 0, "TEdit", null); // 하차지 동읍리
            ____TEdit2 = FindWindowEx(___TPanel5, ____TEdit1, "TEdit", null); // 상차지 동읍리
            ____TEdit3 = FindWindowEx(___TPanel5, ____TEdit2, "TEdit", null); // 하차지 시군
            ____TEdit4 = FindWindowEx(___TPanel5, ____TEdit3, "TEdit", null); // 상차지 시군
            ____TEdit5_more_infomation = FindWindowEx(___TPanel5, ____TEdit4, "TEdit", null); // 추가 정보
            var ____TEdit6 = FindWindowEx(___TPanel5, ____TEdit5_more_infomation, "TEdit", null);

            ____TEdit7_endAddr = FindWindowEx(___TPanel5, ____TEdit6, "TEdit", null);
            ____TEdit8 = FindWindowEx(___TPanel5, ____TEdit7_endAddr, "TEdit", null); // 상차지 시
            ____TEdit9_startAddr = FindWindowEx(___TPanel5, ____TEdit8, "TEdit", null);
            ____TEdit10 = FindWindowEx(___TPanel5, ____TEdit9_startAddr, "TEdit", null); // 하차지 시

            ____TRzComboBox_carsort = FindWindowEx(___TPanel5, 0, "TRzComboBox", null);
            ____TRzComboBox_ton = FindWindowEx(___TPanel5, ____TRzComboBox_carsort, "TRzComboBox", null);
            ____TComboBox_arrival = FindWindowEx(___TPanel5, 0, "TComboBox", null);
            ____TwCombo_payment_method = FindWindowEx(___TPanel5, 0, "TwCombo", null);

            var ____TXiPenal_load_capacity = FindWindowEx(___TPanel5, ____TwCombo_payment_method,
                    "TXiPanel", null);
            ____TEdit_load_capacity = FindWindowEx(____TXiPenal_load_capacity, 0, "TEdit", null);

            ____TwNumEdit_shipping_fee = FindWindowEx(___TPanel5, 0, "TwNumEdit", null);
            ____TwNumEdit_total_price = FindWindowEx(___TPanel5, ____TwNumEdit_shipping_fee, "TwNumEdit",
                    null);
            ____TwNumEdit_commission = FindWindowEx(___TPanel5, ____TwNumEdit_total_price, "TwNumEdit",
                    null);
            ____TMemo = FindWindowEx(___TPanel5, 0, "TMemo", null);
        }

        public void setAddr(InfoModel info)
        {
            closeSearchAddrWindow(0);
            if (____TEdit9_startAddr == 0 || ____TEdit7_endAddr == 0 || ____TRzComboBox_ton == 0 || ____TDBGrid == 0 || ___TBitBtn_newBtn == 0)
            {
                getHWND();
            }
            SendMessage(___TBitBtn_newBtn, 0x00F5, 0, 0); // 신규(F3) 누르기

            SendMessage(____TEdit9_startAddr, WM_SETFOCUS, 0, 0); // 상차지 포커스
            SendMessage(____TEdit9_startAddr, WM_SETTEXT, 0, info.load_addr); // 상차지 텍스트 넣기
            PostMessage(____TEdit9_startAddr, WM_KEYDOWN, VK_RETURN, 1); // 상차지 엔터
            closeSearchAddrWindow(1000);
            PostMessage(____TDBGrid, WM_KEYDOWN, VK_RETURN, 0); // 상차지 리스트 엔터
            SendMessage(____TEdit9_startAddr, WM_KILLFOCUS, 0, 0); // 상차지 포커스 해제
            

            SendMessage(____TEdit7_endAddr, WM_SETFOCUS, 0, 0); // 하차지 포커스
            SendMessage(____TEdit7_endAddr, WM_SETTEXT, 0, info.alight_addr); // 하차지 텍스트 넣기
            PostMessage(____TEdit7_endAddr, WM_KEYDOWN, VK_RETURN, 1); // 하차지 엔터
            closeSearchAddrWindow(1000);
            PostMessage(____TDBGrid, WM_KEYDOWN, VK_RETURN, 0); // 하차지 리스트 엔터
            SendMessage(____TEdit7_endAddr, WM_KILLFOCUS, 0, 0); // 하차지 포커스 해제
            
            SendMessage(____TRzComboBox_ton, CB_SETCURSEL, info.ton_idx, 1);
            Thread.Sleep(100);
        }

        public void setTonCar(InfoModel info)
        {
            try
            {
                Thread.Sleep(100);
                int leftFrameParent = GetWindowLongPtr(____TRzComboBox_ton, -12);
                int send_cbn_selchange = MakeWParam(leftFrameParent, (int)CBN_SELCHANGE);

                // 톤수 설정
                PostMessage(____TRzComboBox_ton, CB_SETCURSEL, info.ton_idx, 0);
                PostMessage(leftFrameParent, WM_COMMAND, send_cbn_selchange, ____TRzComboBox_ton);
                // 차종 설정
                PostMessage(____TRzComboBox_carsort, CB_SETCURSEL, info.car_sort_idx, 0);
                PostMessage(leftFrameParent, WM_COMMAND, send_cbn_selchange, ____TRzComboBox_carsort);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        public void setRegistOption(InfoModel info)
        {
            int leftFrameParent = GetWindowLongPtr(____TRzComboBox_ton, -12);
            int send_cbn_selchange = MakeWParam(leftFrameParent, CBN_SELCHANGE);

            Thread.Sleep(100);

            //			// 도착 설정
            PostMessage(____TComboBox_arrival, CB_SETCURSEL, info.arrival_idx, 0);
            PostMessage(leftFrameParent, WM_COMMAND, send_cbn_selchange, ____TComboBox_arrival);
            // 지불 방식 운송비구분
            sendChar(____TwCombo_payment_method, info.payment_method);
            Thread.Sleep(100);
            // 운송료
            sendChar(____TwNumEdit_shipping_fee, info.price + "");
            Thread.Sleep(100);
            // 수수료
            sendChar(____TwNumEdit_commission, info.commission + "");
            Thread.Sleep(100);
            // 화물 정보
            sendChar(____TEdit5_more_infomation, info.freight_info);

            Thread.Sleep(100);
            sendChar(____TEdit_load_capacity, info.ton_info);

            if (info.mixed_loading.Contains("혼적"))
            {
                if (SendMessage(____TCheckBox_mixup, BM_GETCHECK, 0, 0) == BST_UNCHECKED)
                {
                    SendMessage(____TCheckBox_mixup, (int)BM_CLICK, 0, 0);
                }
            }
            else
            {
                if (SendMessage(____TCheckBox_mixup, BM_GETCHECK, 0, 0) == BST_CHECKED)
                {
                    SendMessage(____TCheckBox_mixup, (int)BM_CLICK, 0, 0);
                }
            }

            if (info.reserved.Contains("예약"))
            {
                if (SendMessage(____TCheckBox_reserved, BM_GETCHECK, 0, 0) == BST_UNCHECKED)
                {
                    SendMessage(____TCheckBox_reserved, (int)BM_CLICK, 0, 0);
                }
            }
            else
            {
                if (SendMessage(____TCheckBox_reserved, BM_GETCHECK, 0, 0) == BST_CHECKED)
                {
                    SendMessage(____TCheckBox_reserved, (int)BM_CLICK, 0, 0);
                }
            }
        }

        public void registBtnClick()
        {
            SendMessageTimeout(___TBitBtn_registBtn, BM_CLICK, 0, 0, SendMessageTimeoutFlag.SMTO_NORMAL, 1);
        }

        public void closeMessageForm()
        {
            var TMessageForm = FindWindow("TMessageForm", null);
            var TButton = FindWindowEx(TMessageForm, 0, "TButton", null);
            PostMessage(TButton, (int)BM_CLICK, 0, 0);
        }

        public string? confirmMessageForm()
        {
            try
            {
                Thread.Sleep(500);
                var TMessageForm = FindWindow("TMessageForm", null);
                while (TMessageForm == 0)
                {
                    TMessageForm = FindWindow("TMessageForm", null);
                }
                Thread.Sleep(500);
                var TButton = FindWindowEx(TMessageForm, 0, "TButton", null);

                var caption = GetControlText(TMessageForm);

                var image = capture(TMessageForm);
                caption += OCR(image);

                PostMessage(TButton, BM_CLICK, 0, 0);
                return caption;
            }
            catch (Exception e)
            {
                return null;
            }
        }

        public void btnClick(int hwnd)
        {
            PostMessage(hwnd, WM_LBUTTONDOWN, MK_LBUTTON, 1);
            PostMessage(hwnd, WM_LBUTTONUP, MK_LBUTTON, 1);
        }

        public void sendChar(int hwnd, string? str)
        {
            if (str == null) { return; }
            PostMessage(hwnd, WM_SETFOCUS, 0, 0);
            for (int i = 0; i < str.Length; i++)
            {
                PostMessage(hwnd, WM_CHAR, str.ElementAt(i), 0);
            }
            PostMessage(hwnd, WM_KEYDOWN, VK_RETURN, 0);
            PostMessage(hwnd, WM_KILLFOCUS, 0, 0);
        }

        public string getLoadAddrText()
        {
            string loadAddr = GetControlText(____TEdit10) + " " + GetControlText(____TEdit4) + " "
                                 + GetControlText(____TEdit2);
            return loadAddr;
        }

        public string getAlightAddrText()
        {
            string alightAddr = GetControlText(____TEdit8) + " " + GetControlText(____TEdit3) + " "
                                 + GetControlText(____TEdit1);
            return alightAddr;
        }

        public string getDistancePrice()
        {
            if (__TXiPanel_distanceAndPrice == 0)
            {
                var TfrmCargoMain = FindWindow("TfrmCargoMain", null);
                var MDIClient = FindWindowEx(TfrmCargoMain, 0, "MDIClient", null);
                var TfrmCargoOrderOld = FindWindowEx(MDIClient, 0, "TfrmCargoOrderOld", null);
                var _TRzPanel = FindWindowEx(TfrmCargoOrderOld, 0, "TRzPanel", null);
                var _TRzPanel2 = FindWindowEx(TfrmCargoOrderOld, _TRzPanel, "TRzPanel", null);
                __TXiPanel_distanceAndPrice = FindWindowEx(_TRzPanel2, 0, "TXiPanel", null); // 거리 금액 표시창
            }

            return OCR(capture(__TXiPanel_distanceAndPrice));
        }

        public void closeSearchAddrWindow(int time)
        {
            Thread.Sleep(time);
            var TfrmAddrSearchXP = FindWindow("TfrmAddrSearchXP", null);
            PostMessage(TfrmAddrSearchXP, WM_CLOSE, 0, 0);
            PostMessage(TfrmAddrSearchXP, WM_CLOSE, 0, 0);
        }

        static int MakeWParam(int loWord, int hiWord)
        {
            return (loWord & 0xFFFF) + ((hiWord & 0xFFFF) << 16);
        }

        public Bitmap capture(int hwnd)
        {
            Rectangle rc = Rectangle.Empty;
            Graphics gfxWin = Graphics.FromHwnd((IntPtr)hwnd);
            rc = Rectangle.Round(gfxWin.VisibleClipBounds);
            Bitmap bmp = new Bitmap(rc.Width, rc.Height, PixelFormat.Format32bppArgb);
            Graphics gfxBmp = Graphics.FromImage(bmp);
            IntPtr hdcBitmap = gfxBmp.GetHdc();
            bool succeeded = PrintWindow(hwnd, (int)hdcBitmap, 0x2);
            gfxBmp.ReleaseHdc(hdcBitmap);
            if (!succeeded)
            {
                gfxBmp.FillRectangle(new SolidBrush(Color.Gray), new Rectangle(Point.Empty, bmp.Size));
            }

            int hRgn = CreateRectRgn(0, 0, 0, 0);
            GetWindowRgn(hwnd, hRgn);
            Region region = Region.FromHrgn((IntPtr)hRgn);
            if (!region.IsEmpty(gfxBmp))
            {
                gfxBmp.ExcludeClip(region);
                gfxBmp.Clear(Color.Transparent);
            }

            gfxBmp.Dispose();
            return bmp;
        }

        public string GetControlText(int hWnd)
        {

            // Get the size of the string required to hold the window title (including trailing null.) 
            var titleSize = SendMessage(hWnd, WM_GETTEXTLENGTH, 0, 0);

            // If titleSize is 0, there is no title so return an empty string (or null)
            if (titleSize == 0)
                return string.Empty;

            StringBuilder title = new StringBuilder(titleSize + 1);

            SendMessage(hWnd, WM_GETTEXT, title.Capacity, title);

            return title.ToString();
        }

        private string OCR(Bitmap bmp)
        {
            var cap = new Bitmap(bmp, new Size(bmp.Size.Width, bmp.Size.Height));

            for (int i = 0; i < cap.Width; i++)
            {
                for (int j = 0; j < cap.Height; j++)
                {
                    Color c = cap.GetPixel(i, j);
                    int binary = (c.R + c.G + c.B) / 3;

                    if (binary > 190)
                        cap.SetPixel(i, j, Color.White);
                    else
                        cap.SetPixel(i, j, Color.Black);
                }
            }

            MemoryStream stream = new MemoryStream();
            bmp.Save(@"confirm_msg.jpg");
            using (var engine = new TesseractEngine(@"./tessdata", "eng+kor", EngineMode.Default))
            {
                using(var img = Pix.LoadFromFile(@"confirm_msg.jpg"))
                {
                    using (var page = engine.Process(img))
                    {
                        return page.GetText();
                    }
                }
                
            }
            /*var ocr = new IronTesseract();
            ocr.Language = OcrLanguage.Korean;
            using (var input = new OcrInput(@"confirm_msg.jpg"))
            {
                var result = ocr.Read(input);
                return result.Text.Replace("\n", "").Replace("T", "").Replace("@", "").Trim();
            }*/
        }
    }
}
