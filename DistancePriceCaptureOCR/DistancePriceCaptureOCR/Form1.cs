using Microsoft.Win32;
using System;
using System.Diagnostics;
using System.Windows.Forms;

namespace DistancePriceCaptureOCR
{
    public partial class Form1 : Form
    {
        TcpServer tcp;
        public Form1()
        {
            InitializeComponent();
            FormClosing += new FormClosingEventHandler(closing);
            tcp = new TcpServer(form: this);
            tcp.start();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void closing(object sender, FormClosingEventArgs e)
        {
            tcp.isRunning = false;
            tcp.stop();
        }

        public void AppendTextBox(string value)
        {
            if (InvokeRequired)
            {
                this.Invoke(new Action<string>(AppendTextBox), new object[] { value });
                return;
            }
            try
            {
                richTextBox1.Text += value;
            } catch
            {

            }
            
        }
    }
}