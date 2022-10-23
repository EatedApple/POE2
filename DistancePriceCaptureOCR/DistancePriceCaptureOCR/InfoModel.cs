using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static System.Windows.Forms.DataFormats;

namespace DistancePriceCaptureOCR
{
    public class InfoModel
    {
        string[] sort0_1 = { "다마스", "라보" };
        string[] sort1_3 = { "차종확인", "전체", "카고", "윙바디", "탑", "초장축", "호루", "냉동탑", "리프트", "리프트윙", "냉장윙", "리프트호", "리프트탑", "초장축호",
            "초장축탑", "초장축윙", "냉장탑", "초장축호리", "초장축탑리", "초장축윙리", "초장축냉동탑", "초냉장윙리", "냉장탑리", "초냉장탑리", "초장축리", "냉동윙", "냉동윙리",
            "냉장윙리", "초장축냉동윙", "초냉동윙리", "초장축냉장윙", "초냉동탑리", "초장축냉장탑" };
        string[] sort5_25 = {"차종확인", "전체", "카고", "윙바디", "탑", "축카고", "초장축", "호루", "냉동탑", "플러스카", "플축카고", "윙축", "리프트", "플러스윙",
            "리프트윙", "플축윙", "냉장윙", "냉장윙축", "리프트호", "리프트탑", "냉장탑", "냉장리플윙", "냉동리플윙", "플축윙리", "플러스윙리", "냉장탑리", "냉장윙축리",
            "냉동윙축리", "축카고리", "윙축리", "플러스리", "냉동윙", "냉동윙리", "냉장윙리", "냉동윙축", "냉장플윙", "냉동플윙", "냉동플축윙리", "냉장플축윙리", "냉동탑플축",
            "냉동탑플", "냉동탑플리", "냉동탑플축리", "냉동탑리", "플축카리", "냉동탑축", "플호리", "플축호리", "플축호루", "플러스호", "플러스탑", "플축탑", "플축탑리",
            "냉장탑플", "냉장탑플축", "냉장탑플축리", "냉장탑플리", "호루축", "냉장플축윙", "냉동플축윙", "플러스탑리" };
        string[] arrival_array = { "선택", "당착", "내착", "월착" };
        double[] ton_array = new double[] { 0.3, 0.5, 1, 1.4, 2.5, 3.5, 5, 8, 11, 14, 15, 18, 25 };
        public string type;
        public string load_addr;
        public string alight_addr;
        public double car_ton;
        public int ton_idx = 0;
        public string ton_info = "";
        public string car_sort;
        public int car_sort_idx = 0;
        public string payment_method = "선/착불";
        public string arrival;
        public int arrival_idx = 0;
        public int count = 1;
        public int price;
        public int commission;
        public int total_price;
        public string freight_info;
        public string[] car_sort_array;
        public string mixed_loading = "";
        public string reserved = "";

        public InfoModel(string data)
        {
            try
            {
                JObject json = JObject.Parse(data);
                JToken token = JToken.FromObject(json);

                if (token["type"] != null)
                {
                    type = token.Value<string>("type");
                }
                if (token["load_addr"] != null)
                {
                    load_addr = token.Value<string>("load_addr");
                }
                if (token["alight_addr"] != null)
                {
                    alight_addr = token.Value<string>("alight_addr");
                }
                if (token["car_ton"] != null)
                {
                    car_ton = token.Value<double>("car_ton");
                    ton_idx = Array.IndexOf(ton_array, car_ton);
                    if (car_ton < 1)
                    {
                        car_sort_array = sort0_1;
                    }
                    else if (car_ton >= 1 && car_ton < 5)
                    {
                        car_sort_array = sort1_3;
                    }
                    else
                    {
                        car_sort_array = sort5_25;
                    }
                    ton_info = string.Format("{0:0.00}", car_ton * 1.1);
                }
                if (token["car_sort"] != null)
                {
                    car_sort = token.Value<string>("car_sort");
                    car_sort_idx = Array.IndexOf(car_sort_array, car_sort);
                }
                if (token["arrival"] != null)
                {
                    arrival = token.Value<string>("arrival");
                }
                if (token["price"] != null)
                {
                    price = token.Value<int>("price");
                }
                if (token["commission"] != null)
                {
                    commission = token.Value<int>("commission");
                }

                if (token["freight_info"] != null)
                {
                    freight_info = token.Value<string>("freight_info");
                }
                if (token["mixed_loading"] != null)
                {
                    mixed_loading = token.Value<string>("mixed_loading");
                }
                if (token["reserved"] != null)
                {
                    reserved = token.Value<string>("reserved");
                }
            } catch (Exception e)
            {
                Debug.WriteLine("########" + e.Message);
                this.load_addr = String.Empty;
                this.alight_addr = String.Empty;
            }
        }
    }
}
