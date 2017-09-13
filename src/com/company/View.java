package com.company;

import io.kaitai.struct.KaitaiStream;

import java.io.*;
import java.util.Arrays;

/*
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

class View extends JFrame {

        private JScrollPane upArea;
        private JScrollPane downArea;

        private JTextArea AllPackets;
        private JTextArea ActivePackets;

        private JComboBox <String> canalLevel;
        private JComboBox <String> networkLevel;
        private JComboBox <String> transportLevel;
        private JComboBox <String> applicationLevel;

        private JTextField portField;
        private int port;
        private int iter=0;
        private StringBuilder filter;

        private JLabel portLabel;
        private JButton launchKey;

        View() throws HeadlessException, IOException {
            settings();

            this.add(upArea);
            this.add(downArea);
            this.add(canalLevel);
            this.add(networkLevel);
            this.add(transportLevel);
            this.add(applicationLevel);
            this.add(portField);
            this.add(portLabel);
            this.add(launchKey);
        }
        private void settings() throws IOException {
            this.setTitle("Bulbumshark");
            this.setIconImage(ImageIO.read(new File("src//com//company//pics//icon.jpg")));
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src//com//company//pics//back.jpg")))));
            this.setBounds(Data.leftIndent * Toolkit.getDefaultToolkit().getScreenSize().width / 100, Data.upIndent * Toolkit.getDefaultToolkit().getScreenSize().height / 100,  Data.frameWidth, Data.frameHeight);
            this.setLayout(null);

            launchKey = new JButton(new ImageIcon("src//com//company//pics//search.jpg"));
            launchKey.setBounds(Data.launchKBounds.getLeft(), Data.launchKBounds.getTop(), Data.launchKBounds.getWidth(), Data.launchKBounds.getHeight());
            launchKey.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    launchKey.setBounds(Data.launchKBounds.getLeft()+5, Data.launchKBounds.getTop()+10, Data.launchKBounds.getWidth()-10, Data.launchKBounds.getHeight()-5);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                 //   launchKey.setBounds(Data.launchKBounds.getLeft(), Data.launchKBounds.getTop(), Data.launchKBounds.getWidth(), Data.launchKBounds.getHeight());
                    try {

                        toParseFromFile();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            AllPackets = new JTextArea();
            AllPackets.setBackground(Color.LIGHT_GRAY);
            AllPackets.setFont(Data.centralFontInAreas);
            AllPackets.setAutoscrolls(true);
            AllPackets.setBorder(BorderFactory.createCompoundBorder());
            AllPackets.setLineWrap(true);
            AllPackets.setWrapStyleWord(true);

            upArea = new JScrollPane(AllPackets);
            upArea.setAutoscrolls(true);
            upArea.setBounds(Data.AllPacketsBounds.getLeft(), Data.AllPacketsBounds.getTop(), Data.AllPacketsBounds.getWidth(), Data.AllPacketsBounds.getHeight());


            ActivePackets = new JTextArea();
            ActivePackets.setBackground(Color.LIGHT_GRAY);
            ActivePackets.setFont(Data.centralFontInAreas);
            ActivePackets.setForeground(Color.GREEN);
            ActivePackets.setBorder(BorderFactory.createCompoundBorder());
            ActivePackets.setLineWrap(true);
            ActivePackets.setWrapStyleWord(true);

            downArea = new JScrollPane(ActivePackets);
            downArea.setAutoscrolls(true);
            downArea.setBounds(Data.ActivePacketsBounds.getLeft(), Data.ActivePacketsBounds.getTop(), Data.ActivePacketsBounds.getWidth(), Data.ActivePacketsBounds.getHeight());

            canalLevel = new JComboBox<>(new String[]{"Ethernet", "ARP"});
            canalLevel.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().toString().equals("ARP"))
                        toMakeOnlyARP();
                    else toMakeOnlyEthernet();

                };

            });
            networkLevel = new JComboBox<>(new String[]{"IPv4"});
            transportLevel = new JComboBox<>(new String[]{"TCP", "UDP"});
            applicationLevel = new JComboBox<>(new String[]{"HTTP", "DNS", "Other"});

            canalLevel.setBounds(Data.canalLBounds.getLeft(), Data.canalLBounds.getTop(), Data.canalLBounds.getWidth(), Data.canalLBounds.getHeight());
            networkLevel.setBounds(Data.networkLBounds.getLeft(), Data.networkLBounds.getTop(), Data.networkLBounds.getWidth(), Data.networkLBounds.getHeight());
            transportLevel.setBounds(Data.transportLBounds.getLeft(), Data.transportLBounds.getTop(), Data.transportLBounds.getWidth(), Data.transportLBounds.getHeight());
            applicationLevel.setBounds(Data.applicationLBounds.getLeft(), Data.applicationLBounds.getTop(), Data.applicationLBounds.getWidth(), Data.applicationLBounds.getHeight());

            portField = new JTextField();
            portField.setFont(Data.centralFontInKeys);
            portField.setBorder(BorderFactory.createCompoundBorder());
            portField.setBounds(Data.portFBounds.getLeft(), Data.portFBounds.getTop(), Data.portFBounds.getWidth(), Data.portFBounds.getHeight());

            portLabel = new JLabel("port");
            portLabel.setFont(Data.centralFontInKeys);
            portLabel.setBounds(Data.portLBounds.getLeft(), Data.portLBounds.getTop(), Data.portLBounds.getWidth(), Data.portLBounds.getHeight());

            filter = new StringBuilder();

            this.setVisible(true);
        }

    private void toMakeOnlyARP() {
        this.remove(networkLevel);
        this.remove(applicationLevel);
        this.remove(transportLevel);

        this.repaint();
    }
    private void toMakeOnlyEthernet(){
            this.add(networkLevel);
            this.add(applicationLevel);
            this.add(transportLevel);

            this.repaint();
    }

    private void toParseFromFile() throws IOException {
        AllPackets.setBackground(Color.WHITE);
        ActivePackets.setBackground(Color.WHITE);

        EthernetFrame frame;
        Ipv4Packet ipv4;
        TcpSegment tcp;
        UdpDatagram udp;

        Pcap p = Pcap.fromFile("material/log114MB.pcap");
        KaitaiStream io = p._io();

        while (!io.isEof()) {
            Pcap.Packet packet = new Pcap.Packet(io, p, p);

        }
    }
}
/*

*/

/*

                          if (ipv4.body().getClass().toString().endsWith("TcpSegment")){
                              tcp = (TcpSegment) ipv4.body();

                            log.append("TCP:" + "\nsrcPort: ").append(tcp.srcPort()).append("\ndestPort: ").append(tcp.dstPort()).append("\nseqNum: ").append(tcp.seqNum()).append("\nackNum: ").append(tcp.ackNum()).append("\nb12: ").append(tcp.b12()).append("\nb13: ").append(tcp.b13()).append("\nwindowSize: ").append(tcp.windowSize()).append("\nchecksum: ").append(tcp.checksum()).append("\nurgentPointer").append(tcp.urgentPointer()).append("\ntcp body: ").append(Arrays.toString(tcp.body())).append("\n\n");
                        }

                        if (ipv4.body().getClass().toString().endsWith("UdpDatagram")){
                            udp = (UdpDatagram) ipv4.body();

                          log.append("UDP:" + "\nsrcPort: ").append(udp.srcPort()).append("\ndestPort: ").append(udp.dstPort()).append("\nlength: ").append(udp.length()).append("\nchecksum: ").append(udp.checksum()).append("\nudp body: ").append(Arrays.toString(udp.body())).append("\n\n");
                        }
                    }
                }
    }

        System.out.println(log);
    }
}
    /*
    private void run(){
             AllPackets.setBackground(Color.WHITE);
             ActivePackets.setBackground(Color.WHITE);


            java.util.List<PcapIf> alldevs = new ArrayList<>(); // Will be filled with NICs
            StringBuilder errbuf = new StringBuilder(); // For any error msgs

            int r = Pcap.findAllDevs(alldevs, errbuf);
            if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
                System.err.printf("Can't read list of devices, error is %s", errbuf
                        .toString());
                return;
            }

            System.out.println("Network devices found:");

            int i = 0;
            for (PcapIf device : alldevs) {
                String description =
                        (device.getDescription() != null) ? device.getDescription()
                                : "No description available";
                System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
            }

            PcapIf device = alldevs.get(0); // We know we have atleast 1 device
            System.out
                    .printf("\nChoosing '%s' on your behalf:\n",
                            (device.getDescription() != null) ? device.getDescription()
                                    : device.getName());

            int snaplen = 64 * 1024;           // Capture all packets, no trucation
            int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
            int timeout = 10 * 1000;           // 10 seconds in millis
            Pcap pcap =
                    Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

            if (pcap == null) {
                System.err.printf("Error while opening device for capture: "
                        + errbuf.toString());
                return;
            }
      PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

                final Ethernet ethernet = new Ethernet();
                final Arp arp = new Arp();
                final Ip4 ip4 = new Ip4();
                final Tcp tcp = new Tcp();
                final Udp udp = new Udp();
                final Http http = new Http();

                public void nextPacket(PcapPacket packet, String user) {

                    AllPackets.append(++iter+". ");
                    AllPackets.append("Received packet at "+
                            new Date(packet.getCaptureHeader().timestampInMillis())+
                            ", length: "+
                            packet.getCaptureHeader().caplen()+  // Length actually captured
                            "/"+
                            packet.getCaptureHeader().wirelen()+ // Original length
                            " b.: "+
                            user+"\n"                                 // User supplied object
                    );

                    filter.delete(0, filter.length());


                    if (canalLevel.getSelectedIndex() == 0)
                        if (packet.hasHeader(Ethernet.ID)){
                                                          //packet.getHeader(ethernet);
                                                          filter.append(iter).append(". ETHERNET -> ");

                                                                 if (networkLevel.getSelectedIndex() == 0){
                                                                            if (packet.hasHeader(Ip4.ID))
                                                                           //     packet.getHeader(ip4);
                                                                                filter.append(" IPv4 -> ");


                                                                                if (transportLevel.getSelectedIndex() == 0)
                                                                                           if (packet.hasHeader(Tcp.ID)){
                                                                                            //   packet.getHeader(tcp);
                                                                                               filter.append("TCP -> ");

                                                                                                       if (applicationLevel.getSelectedIndex() == 0)
                                                                                                               if (packet.hasHeader(Http.ID)){
                                                                                                                   filter.append("Http\n");

                                                                                                                   ActivePackets.append(filter.toString());
                                                                                                                   downArea.revalidate();
                                                                                                                   downArea.repaint();

                                                                                                               }

                                                                                           }

                                                                                if (transportLevel.getSelectedIndex() == 1)
                                                                                           if (packet.hasHeader(Udp.ID)){
                                                                                              // packet.getHeader(udp);

                                                                                                       if (applicationLevel.getSelectedIndex() == 0)
                                                                                                           if (packet.hasHeader(Http.ID)){
                                                                                                              filter.append("UDP -> Http\n");

                                                                                                               ActivePackets.append(filter.toString());
                                                                                                               downArea.revalidate();
                                                                                                               downArea.repaint();
                                                                                                          }
                                                                                                          else {
                                                                                                               filter.append("UDP\n");
                                                                                                               ActivePackets.append(filter.toString());
                                                                                                               downArea.revalidate();
                                                                                                               downArea.repaint();
                                                                                                           }

                                                                                           }


                                                          }
                        }


                    /*if (canalLevel.getSelectedIndex() == 1)
                        System.out.println(packet.getHeader(arp).toString());
                    {
//                        packet.getHeader(arp);
                        ActivePackets.append(iter+". ARP (v."+arp.protocolType()+") -> \n");

                        downArea.revalidate();
                        downArea.repaint();
                    }
                }

                public void toPrintLog() throws FileNotFoundException {
                    FileOutputStream fos = new FileOutputStream("parsed.log");
                    PrintStream printStream = new PrintStream(fos);
                    printStream.println(ActivePackets);
                }
            };

            System.out.println(canalLevel.getSelectedIndex());
            System.out.println(networkLevel.getSelectedIndex());
            System.out.println(transportLevel.getSelectedIndex());
            System.out.println(applicationLevel.getSelectedIndex());

            pcap.loop(3000, jpacketHandler, "interface #0");
            pcap.close();

        }*/
