using Newtonsoft.Json.Linq;
using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;

namespace DistancePriceCaptureOCR
{

    internal class TcpServer
    {
        Form1 form;
        FindProcess fp;
        TcpListener listener;
        public bool isRunning;
        public TcpServer(Form1 form)
        {
            this.form = form;
            fp = new FindProcess(form);
        }

        public void start() 
        {
            isRunning = true;
            Thread st = new Thread(startServer);
            st.Start();
        }

        public void stop()
        {
            isRunning = false;
            listener.Stop();
        }

        public void test()
        {
            var car_ton = 0.5;

            Debug.WriteLine(string.Format("{0:0.0}", car_ton * 1.1));

            car_ton = 1.4;
            Debug.WriteLine(string.Format("{0:0.0}", car_ton * 1.1));

            car_ton = 2.5;
            Debug.WriteLine(string.Format("{0:0.0}", car_ton * 1.1));

            car_ton = 3.5;
            Debug.WriteLine(string.Format("{0:0.0}", car_ton * 1.1));

            car_ton = 5;
            Debug.WriteLine(string.Format("{0:0.0}", car_ton * 1.1));

            car_ton = 8;
            Debug.WriteLine(string.Format("{0:0.0}", car_ton * 1.1));

            car_ton = 11;
            Debug.WriteLine(string.Format("{0:0.0}", car_ton * 1.1));
        }
        private void startServer()
        {
            test();
            listener = new TcpListener(IPAddress.Any, 8000);
            listener.Server.ReceiveTimeout = 10000;
            listener.Start();

            byte[] buff = new byte[1024];
            form.AppendTextBox("SERVER RUNNING...\n");

            while (isRunning)
            {
                TcpClient tc = listener.AcceptTcpClient();
                var clientIP = tc.Client.RemoteEndPoint.ToString();
                if (!clientIP.Contains("112.175.243.19") && !clientIP.Contains("192.168.0.1"))
                {
                    tc.Close();
                    continue;
                }

                form.AppendTextBox(String.Format("[{0}] CONNECTED\n", clientIP));
                NetworkStream stream = tc.GetStream();
                int i;

                try
                {
                    while ((i = stream.Read(buff, 0, buff.Length)) > 0)
                    {
                        string data = System.Text.Encoding.Default.GetString(buff, 0, i);

                        try
                        {
                            var info = new InfoModel(data);
                            form.AppendTextBox(String.Format("상:{0}, 하: {1}, 톤: {2}\n\n", info.load_addr, info.alight_addr, info.car_ton));

                            fp.closeMessageForm();
                            fp.setAddr(info);
                            var loadAddr = fp.getLoadAddrText();
                            var alightAddr = fp.getAlightAddrText();

                            fp.setTonCar(info);
                            JObject res = new JObject(
                                new JProperty("req_load_addr", info.load_addr),
                                new JProperty("req_alight_addr", info.alight_addr),
                                new JProperty("res_load_addr", loadAddr),
                                new JProperty("res_alight_addr", alightAddr),
                                new JProperty("proc", 1)
                            );

                            if (info.load_addr != info.alight_addr)
                            {
                                if (loadAddr == alightAddr)
                                {
                                    throw new Exception("상하차지 설정 실패");
                                }
                            }

                            if (info.type == "regist") {
                                fp.setRegistOption(info);
                                fp.registBtnClick();
                                string confirmMsg = fp.confirmMessageForm();
                                string msg = "";

                                if (confirmMsg == null)
                                {
                                    msg += "등록실패";
                                    res["proc"] = 0;
                                }
                                else
                                {
                                    if (confirmMsg.Contains("Error") || !confirmMsg.Contains("접수"))
                                    {
                                        res["proc"] = 0;
                                    }
                                    msg += confirmMsg;
                                }

                                string log = "등록\n[params]" + data +"\n" + "[msg]" + msg + "\n"
                                        + "[res]" + res.ToString();
                                res.Add("msg", msg.Replace("\n", ""));
                                form.AppendTextBox("[ " + clientIP + " ] " + log + "\n");
                            } else if (info.type == "search")
                            {
                                var ocrText = fp.getDistancePrice();
                                var price = ocrText.Replace("?", "7").Split(':')[2];
                                price = Regex.Replace(price, @"\D", "");

                                res.Add("ocr", ocrText);
                                res.Add("price", price);

                                string log = "등록\n[params]" + data + "\n" + "[msg]" + "[res]" + res.ToString();
                                form.AppendTextBox("[ " + clientIP + " ] " + log + "\n");
                            }

                            var result = Encoding.UTF8.GetBytes(res.ToString());
                            stream.Write(result, 0, result.Length);
                        }
                        catch (Exception ex)
                        {
                            byte[] msg = System.Text.Encoding.Default.GetBytes(ex.Message);
                            stream.Write(msg, 0, msg.Length);
                            Debug.WriteLine(ex.Message);
                        }
                        stream.Close();
                        tc.Close();
                        break;
                    }
                } catch (Exception ex)
                {
                    byte[] msg = System.Text.Encoding.Default.GetBytes(ex.Message);
                    stream.Write(msg, 0, msg.Length);
                    stream.Close();
                    tc.Close();
                }
            }
        }
    }
}