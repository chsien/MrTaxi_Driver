package com.MrTaxi.net.implement;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.MrTaxi.net.sipdroidRTP.RtpPacket;

import android.net.LocalSocket;
import android.os.SystemClock;


public class RTPthread{
    private Thread t;
    private final int fps = 5;
    private boolean change;
    
	public void start(final LocalSocket receiver, final String HOST_IP, final String HOST_PORT)
	{	    
        (t = new Thread() {
			public void run()
			{		        
				int frame_size = 1400;
				byte[] buffer = new byte[frame_size + 14];
				buffer[12] = 4;
				RtpPacket rtp_packet = new RtpPacket(buffer, 0);
				
				InputStream fis = null;
				try {
   					fis = receiver.getInputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				rtp_packet.setPayloadType(103);

				int seqn = 0;
				int num,number = 0,src,dest,len = 0,head = 0,lasthead = 0,lasthead2 = 0,cnt = 0,stable = 0;
				long now,lasttime = 0;
				double avgrate = 45000;
				double avglen = avgrate/20;

		        //udp-RTP socket
		        int RTP_PORT_SENDING = 8000;
		        int RTP_PORT_DEST = Integer.parseInt(HOST_PORT);
		        InetAddress PC_IpAddress = null;
		        try { 
		        	PC_IpAddress = InetAddress.getByName(HOST_IP);
		        } 
		        catch (UnknownHostException e){
		        	e.printStackTrace();
		        }		        
		        DatagramSocket socketSendRPT = null;
		        DatagramPacket RtpPocket = new DatagramPacket(rtp_packet.packet, rtp_packet.packet_len, PC_IpAddress, RTP_PORT_DEST);
		        try{
		        	socketSendRPT = new DatagramSocket(RTP_PORT_SENDING);
		        }
		        catch (SocketException e){
		        	e.printStackTrace();
		        }
		        //
		        
				while (!isInterrupted()) {
					num = -1;
					
					try {
						num = fis.read(buffer,14+number,frame_size-number);
					} catch (IOException e) {
						break;
					}
					
					if (num < 0) {
						
						try {
							sleep(10);
						} catch (InterruptedException e) {
							break;
						}
						
						continue;							
					}
					
					number += num;
					head += num;
					
					try {
						now = SystemClock.elapsedRealtime();
						if (lasthead != head+fis.available() && ++stable >= 5 && now-lasttime > 700) {
							if (cnt != 0 && len != 0)
								avglen = len/cnt;
							if (lasttime != 0) {
								//fps = (int)((double)cnt*1000/(now-lasttime));
								avgrate = (double)((head+fis.available())-lasthead2)*1000/(now-lasttime);
							}
							lasttime = now;
							lasthead = head+fis.available();
							lasthead2 = head;
							len = cnt = stable = 0;
						}
					} catch (IOException e1) {
						break;
					}
					
					for (num = 14; num <= 14+number-2; num++)
						if (buffer[num] == 0 && buffer[num+1] == 0) break;
					if (num > 14+number-2) {
						num = 0;
						rtp_packet.setMarker(false);
					} else {	
						num = 14+number - num;
						rtp_packet.setMarker(true);
					}
					
		 			rtp_packet.setSequenceNumber(seqn++);
		 			rtp_packet.setPayloadLength(number-num+2);
		 			if (seqn > 10)
		 			{
		 				//RtpPocket = new DatagramPacket(rtp_packet.packet, rtp_packet.packet_len, PC_IpAddress, RTP_PORT_DEST);	 				
		 				RtpPocket.setData(rtp_packet.packet);
		 				RtpPocket.setLength(rtp_packet.packet_len);
		 				
	        			try {
	        				socketSendRPT.send(RtpPocket);
	        				len += number-num;
	        			} catch (IOException e) {
	        				e.printStackTrace();
						}
		 				
		 			}
					
		 			if (num > 0) {
			 			num -= 2;
			 			dest = 14;
			 			src = 14+number - num;
			 			if (num > 0 && buffer[src] == 0) {
			 				src++;
			 				num--;
			 			}
			 			number = num;
			 			while (num-- > 0)
			 				buffer[dest++] = buffer[src++];
						buffer[12] = 4;
						
						cnt++;
						
						try {
							if (avgrate != 0)
								Thread.sleep((int)(avglen/avgrate*2000));
						} catch (Exception e) {
							break;
						}
						
			 			rtp_packet.setTimestamp(SystemClock.elapsedRealtime()*90);
		 			} else {
		 				number = 0;
						buffer[12] = 0;
		 			}
		 			if (change) {
		 				change = false;
		 				long time = SystemClock.elapsedRealtime();
		 				
    					try {
							while (fis.read(buffer,14,frame_size) > 0 &&
									SystemClock.elapsedRealtime()-time < 3000);
						} catch (Exception e) {
						}
		 				number = 0;
						buffer[12] = 0;
		 			}
				}
				
				socketSendRPT.close();
				
				try {
					while (fis.read(buffer,0,frame_size) > 0);
				} catch (IOException e) {
					e.printStackTrace();  
				}
			}
		}).start();
	}
	public void stop()
	{		
	    if (t != null)
	        t.interrupt();
	}
}