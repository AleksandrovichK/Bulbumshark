package com.company;

import io.kaitai.struct.KaitaiStream;

import java.io.*;

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

class View extends JFrame {

    private JScrollPane upArea;
    private JScrollPane downArea;

    private JTextArea AllPackets;
    private JTextArea ActivePackets;
    private JTextArea StackProtocols;

    private JComboBox<String> comboProtocols;

    private JTextField portField;
    private int port;
    private int iter = 0;
    private StringBuilder filtered;

    private JLabel portLabel;
    private JButton searchKey;
    private JButton addKey;
    private JButton trashKey;

    View() throws HeadlessException, IOException {
        settings();

        this.add(upArea);
        this.add(downArea);
        this.add(StackProtocols);
        this.add(searchKey);
        this.add(addKey);
        this.add(trashKey);
    }

    private void settings() throws IOException {
        this.setTitle("Bulbumshark");
        this.setIconImage(ImageIO.read(new File("src//com//company//pics//icon.jpg")));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src//com//company//pics//back_2.jpg")))));
        this.setBounds(Data.leftIndent * Toolkit.getDefaultToolkit().getScreenSize().width / 100, Data.upIndent * Toolkit.getDefaultToolkit().getScreenSize().height / 100, Data.frameWidth, Data.frameHeight);
        this.setLayout(null);

        AllPackets = new JTextArea();
        AllPackets.setBackground(Data.centralColor);
        AllPackets.setFont(Data.centralFontInAreas);
        AllPackets.setAutoscrolls(true);
        AllPackets.setBorder(BorderFactory.createCompoundBorder());
        AllPackets.setLineWrap(true);
        AllPackets.setWrapStyleWord(true);
        AllPackets.setEditable(false);

        upArea = new JScrollPane(AllPackets);
        upArea.setAutoscrolls(true);
        upArea.setBounds(Data.AllPacketsBounds.getLeft(), Data.AllPacketsBounds.getTop(), Data.AllPacketsBounds.getWidth(), Data.AllPacketsBounds.getHeight());

        ActivePackets = new JTextArea();
        ActivePackets.setBackground(Data.centralColor);
        ActivePackets.setFont(Data.centralFontInAreas);
        ActivePackets.setForeground(Color.GREEN);
        ActivePackets.setBorder(BorderFactory.createCompoundBorder());
        ActivePackets.setLineWrap(true);
        ActivePackets.setWrapStyleWord(true);
        ActivePackets.setEditable(false);

        downArea = new JScrollPane(ActivePackets);
        downArea.setAutoscrolls(true);
        downArea.setBounds(Data.ActivePacketsBounds.getLeft(), Data.ActivePacketsBounds.getTop(), Data.ActivePacketsBounds.getWidth(), Data.ActivePacketsBounds.getHeight());


        StackProtocols = new JTextArea();
        StackProtocols.setBackground(Data.centralColor);
        StackProtocols.setFont(Data.centralFontInAreas);
        StackProtocols.setAutoscrolls(true);
        StackProtocols.setBorder(BorderFactory.createCompoundBorder());
        StackProtocols.setLineWrap(true);
        StackProtocols.setWrapStyleWord(true);
        StackProtocols.setEditable(false);
        StackProtocols.setBounds(Data.StackProtocolsBounds.getLeft(), Data.StackProtocolsBounds.getTop(), Data.StackProtocolsBounds.getWidth(), Data.StackProtocolsBounds.getHeight());

        comboProtocols = new JComboBox<>(new String[]{"Ethernet", "ARP", "IPv4", "IPv6","TCP", "UDP","HTTP", "DNS", "SSL/TLS"});
        comboProtocols.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getItem().toString().equals("ARP")) System.out.println("Arp");
                    //toMakeOnlyARP();
                //else toMakeOnlyEthernet();

            }
            ;

        });
        comboProtocols.setBounds(Data.comboPBounds.getLeft(), Data.comboPBounds.getTop(), Data.comboPBounds.getWidth(), Data.comboPBounds.getHeight());

        searchKey = new JButton(new ImageIcon("src//com//company//pics//search.png"));
        searchKey.setBounds(Data.searchKBounds.getLeft(), Data.searchKBounds.getTop(), Data.searchKBounds.getWidth(), Data.searchKBounds.getHeight());
        searchKey.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                searchKey.setBounds(searchKey.getX() + 2, searchKey.getY() + 2, searchKey.getWidth() - 2, searchKey.getHeight() - 2);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                searchKey.setBounds(searchKey.getX() - 2, searchKey.getY() - 2, searchKey.getWidth() + 2, searchKey.getHeight() + 2);

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

        trashKey = new JButton(new ImageIcon("src//com//company//pics//trash_2.png"));
        trashKey.setBounds(Data.trashKBounds.getLeft(), Data.trashKBounds.getTop(), Data.trashKBounds.getWidth(), Data.trashKBounds.getHeight());
        trashKey.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                trashKey.setBounds(trashKey.getX() + 2, trashKey.getY() + 2, trashKey.getWidth() - 2, trashKey.getHeight() - 2);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                trashKey.setBounds(trashKey.getX() - 2, trashKey.getY() - 2, trashKey.getWidth() + 2, trashKey.getHeight() + 2);
                AllPackets.setText("");
                iter=0;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addKey = new JButton(new ImageIcon("src//com//company//pics//add.png"));
        addKey.setBounds(Data.addKBounds.getLeft(), Data.addKBounds.getTop(), Data.addKBounds.getWidth(), Data.addKBounds.getHeight());
        addKey.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                addKey.setBounds(addKey.getX() + 5, addKey.getY() + 5, addKey.getWidth() - 5, addKey.getHeight() - 5);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                addKey.setBounds(addKey.getX() - 5, addKey.getY() - 5, addKey.getWidth() + 5, addKey.getHeight() + 5);
                toShowWhatToAdd();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        filtered = new StringBuilder();

        this.setVisible(true);
    }

    private void toShowWhatToAdd(){
        if (!comboProtocols.isShowing()) this.add(comboProtocols);
        this.repaint();
    }
    private void toParseFromFile() throws IOException {
        AllPackets.setBackground(Color.WHITE);
        ActivePackets.setBackground(Color.WHITE);
        //Vector <Pcap.Packet> packets = new Vector<>();

        EthernetFrame frame;
        Ipv4Packet ipv4;
        Ipv6Packet ipv6;
        TcpSegment tcp;
        UdpDatagram udp;

        Pcap p = Pcap.fromFile("material/log4MB.pcap");
        KaitaiStream io = p._io();

        while (!io.isEof()) {
            Pcap.Packet packet = new Pcap.Packet(io, p, p);
            //packets.add(packet);

            frame = (EthernetFrame) packet.body();

            switch (frame.etherType().toString()){
                case "IPV4" : {
                    ipv4 = (Ipv4Packet) frame.body();
                    AllPackets.append("Packet #"+(++iter)+": Ehternet -> IPv4 -> "+ipv4.protocol()+"\n");

                    break;
                }

                case "IPV6" : {
                    ipv6 = (Ipv6Packet) frame.body();
                    AllPackets.append("Packet #"+(++iter)+": Ehternet -> IPv6 -> "+ipv6.nextHeader().getClass().getSimpleName()+"\n");

                    break;
                }

                default: {break;}
            }
        }
        //System.out.println("ALL PACKETS HAD BEEN READ: "+packets.size());
        }
}
/*
        private void toMakeOnlyARP() {
        this.remove(networkLevel);
          this.remove(applicationLevel);
          this.remove(transportLevel);

          this.repaint();
      }
      private void toMakeOnlyEthernet() {
         this.add(networkLevel);
          this.add(applicationLevel);
          this.add(transportLevel);

          this.repaint();
      }
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
*/
