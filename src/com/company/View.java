package com.company;

import io.kaitai.struct.KaitaiStream;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

class View extends JFrame {
    private JScrollPane upArea;
    private JScrollPane downArea;

    private JTextArea AllPackets;
    private JTextArea ActivePackets;
    private JTextArea StackProtocols;

    private Vector <String> vectorOfProtocols;
    private Vector <String> vectorOfInputProtocols;

    private JComboBox<String> comboProtocols;
    private String[] protocols = {"Ethernet", "IPv4", "IPv6","TCP", "UDP","HTTP", "DNS", "SSL/TLS"};

    private int iter = 0;
    private int used = 0;
    private StringBuffer filtered;

    private JButton searchKey;
    private JButton addKey;
    private JButton trashKey;
    private JButton filterKey;
    private JButton clearStackKey;

    View() throws HeadlessException, IOException {
        settings();

        this.add(upArea);
        this.add(downArea);
        this.add(StackProtocols);
        this.add(comboProtocols);
        this.add(searchKey);
        this.add(addKey);
        this.add(clearStackKey);
        this.add(trashKey);
        this.add(filterKey);
    }

    private void settings() throws IOException {
        this.setTitle("Bulbumshark");
        this.setIconImage(ImageIO.read(new File("src//com//company//pics//icon.jpg")));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src//com//company//pics//back9.png")))));
        this.setBounds(Data.leftIndent * Toolkit.getDefaultToolkit().getScreenSize().width / 100, Data.upIndent * Toolkit.getDefaultToolkit().getScreenSize().height / 100, Data.frameWidth, Data.frameHeight);
        this.setLayout(null);

        vectorOfProtocols = new Vector<>();
        vectorOfInputProtocols = new Vector<>();

        toReadConfig();

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
        StackProtocols.setFont(new Font("Microsoft JhengHei Light", Font.BOLD, 13));
        StackProtocols.setAutoscrolls(true);
        StackProtocols.setBorder(BorderFactory.createCompoundBorder());
        StackProtocols.setLineWrap(true);
        StackProtocols.setWrapStyleWord(true);
        StackProtocols.setEditable(false);
        StackProtocols.setBounds(Data.StackProtocolsBounds.getLeft(), Data.StackProtocolsBounds.getTop(), Data.StackProtocolsBounds.getWidth(), Data.StackProtocolsBounds.getHeight());

        comboProtocols = new JComboBox<>(protocols);
        comboProtocols.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                //if (e.getItem().toString().equals("ARP")) System.out.println("Arp");
            }
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
                AllPackets.setBackground(Color.WHITE);
                searchKey.setBounds(searchKey.getX() - 2, searchKey.getY() - 2, searchKey.getWidth() + 2, searchKey.getHeight() + 2);

                try {
                    toLookPreview();

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

        trashKey = new JButton(new ImageIcon("src//com//company//pics//trash.png"));
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
                AllPackets.setBackground(Data.centralColor);
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
        addKey.addMouseListener(new MouseListener(){
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
                StackProtocols.setBackground(Color.WHITE);

                if (StackProtocols.getText().equals("No protocols chosen!")) StackProtocols.setText("");

                StackProtocols.append(""+comboProtocols.getSelectedItem()+'\n');
                vectorOfProtocols.add(comboProtocols.getSelectedItem().toString());
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        clearStackKey = new JButton(new ImageIcon("src//com//company//pics//clearstack.png"));
        clearStackKey.setBounds(Data.clStackKBounds.getLeft(), Data.clStackKBounds.getTop(), Data.clStackKBounds.getWidth(), Data.clStackKBounds.getHeight());
        clearStackKey.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                clearStackKey.setBounds(clearStackKey.getX() + 5, clearStackKey.getY() + 5, clearStackKey.getWidth() - 5, clearStackKey.getHeight() - 5);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                StackProtocols.setBackground(Data.centralColor);
                clearStackKey.setBounds(clearStackKey.getX() - 5, clearStackKey.getY() - 5, clearStackKey.getWidth() + 5, clearStackKey.getHeight() + 5);
                StackProtocols.setText("");
                vectorOfProtocols.clear();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        filterKey = new JButton(new ImageIcon("src//com//company//pics//filter.png"));
        filterKey.setBounds(Data.filterKBounds.getLeft(), Data.filterKBounds.getTop(), Data.filterKBounds.getWidth(), Data.filterKBounds.getHeight());
        filterKey.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                filterKey.setBounds(filterKey.getX() + 2, filterKey.getY() + 2, filterKey.getWidth() - 2, filterKey.getHeight() - 2);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                filterKey.setBounds(filterKey.getX() - 2, filterKey.getY() - 2, filterKey.getWidth() + 2, filterKey.getHeight() + 2);

                if (!vectorOfProtocols.isEmpty()){
                ActivePackets.setBackground(Color.WHITE);
                    try {toFiltrate();} catch (IOException e1) {e1.printStackTrace();}
                }
                else StackProtocols.setText("No protocols chosen!");
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        filtered = new StringBuffer();

        this.setVisible(true);
    }

    private void toReadConfig() throws IOException {
        InputStream inputStream = Main.class.getResourceAsStream("/config/protocols.txt");

        if (inputStream != null) //NO FILE WITH CONFIGURATION
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
            {
                while (!line.equals("</pr>") && !line.equals("</protocols>")){
                    if (!line.isEmpty() && line.charAt(0) != '<')
                           if (line.substring(0, line.indexOf('=')).equals("name"))
                                vectorOfInputProtocols.addElement(line.substring(line.indexOf('\"')+1,line.lastIndexOf('\"')));
                    line = reader.readLine();
                }
            }
            inputStream.close();
            System.out.println(vectorOfInputProtocols);
        }
        else {
            System.out.println("Error: not found file with protocols!");
        }
    }

    private void toLookPreview() throws IOException {
        EthernetFrame frame;
        Ipv4Packet ipv4;
        Ipv6Packet ipv6;
        TcpSegment tcp;
        UdpDatagram udp;
        DnsPacket dns;
        TlsClientHello tls;

        Pcap p = Pcap.fromFile("material/log4MB.pcap");
        KaitaiStream io = p._io();

        while (!io.isEof()) {
            Pcap.Packet packet = new Pcap.Packet(io, p, p);

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
        System.out.println("ALL PACKETS HAD BEEN READ: ");
        }
    private void toFiltrate() throws IOException {
        iter = 0;
        ActivePackets.setText("");

        EthernetFrame frame = null;
        Ipv4Packet ipv4 = null;
        Ipv6Packet ipv6 = null;
        TcpSegment tcp = null;
        UdpDatagram udp = null;
        DnsPacket dns = null;
        TlsClientHello tls = null;

        Pcap p = Pcap.fromFile("material/log4MB.pcap");
        KaitaiStream io = p._io();

        while (!io.isEof()) {
            Pcap.Packet packet = new Pcap.Packet(io, p, p);
            Object container = packet.body();

            filtered.delete(0, filtered.length());
            used = 0;

            for (int protIter = 0; protIter < vectorOfProtocols.size(); protIter++) {
                switch (vectorOfProtocols.get(protIter)) {
                    case "Ethernet": {
                            if (container.getClass().getSimpleName().equals("EthernetFrame")){
                                filtered.append(protIter==0? (++iter)+". Ethernet": " -> Ethernet");
                                filtered.append(protIter == vectorOfProtocols.size()-1? "\n" : "");

                                frame = (EthernetFrame) container;
                                container = frame.body();
                                used++;
                            }
                        break;
                    }

                    case "IPv4": {
                        if (container.getClass().getSimpleName().equals("Ipv4Packet")){
                            filtered.append(protIter==0? (++iter)+". IPv4": " -> IPv4");
                            filtered.append(protIter == vectorOfProtocols.size()-1? "\n" : "");

                            ipv4 = (Ipv4Packet) container;
                            container = ipv4.body();
                            used++;
                        }
                        break;
                    }
                    case "IPv6": {
                        if (container.getClass().getSimpleName().equals("Ipv6Packet")){
                            filtered.append(protIter==0? (++iter)+". IPv6": " -> IPv6");
                            filtered.append(protIter == vectorOfProtocols.size()-1? "\n" : "");

                            ipv6 = (Ipv6Packet) container;
                            container = ipv6.nextHeader();
                            used++;
                        }
                        break;
                    }
                    case "TCP": {
                        if (container.getClass().getSimpleName().equals("TcpSegment")){
                            filtered.append(protIter==0? (++iter)+". TCP": " -> TCP");
                            filtered.append(protIter == vectorOfProtocols.size()-1? "\n" : "");

                            tcp = (TcpSegment) container;
                            container = tcp.body();
                            used++;
                        }
                        break;
                    }
                    case "UDP": {
                        if (container.getClass().getSimpleName().equals("UdpDatagram")){
                            filtered.append(protIter==0? (++iter)+". UDP": " -> UDP");
                            filtered.append(protIter == vectorOfProtocols.size()-1? "\n" : "");

                            udp = (UdpDatagram) container;
                            //container = udp.body();
                            used++;
                        }
                        break;
                    }
                    case "HTTP": {

                    }

                    case "DNS": {
                        if (container.getClass().getSimpleName().equals("UdpDatagram")){
                            udp = (UdpDatagram) container;
                            if (udp.dstPort() == 53){
                                filtered.append(protIter==0? (++iter)+". DNS": " -> DNS (Query)");
                                filtered.append(protIter == vectorOfProtocols.size()-1? "\n" : "");
                                used++;
                            }

                                if (udp.srcPort() == 53){
                                    filtered.append(protIter==0? (++iter)+". DNS": " -> DNS (Answer)");
                                    filtered.append(protIter == vectorOfProtocols.size()-1? "\n" : "");
                                    used++;

                                }
                            }
                        break;
                        }
                    case "SSL/TLS": {
                    }
                    default:{
                        System.err.println(iter+": NO SUCH PROTOCOL FOUND!");
                    }
                  }

             if (used == vectorOfProtocols.size()) ActivePackets.append(filtered.toString());
            }
        }

//                                ipv6 = (Ipv6Packet) frame.body();
  //                              ActivePackets.append("Packet #" + (++iter) + ": Ehternet -> IPv6 -> " + ipv6.nextHeader().getClass().getSimpleName() + "\n");

    }
}