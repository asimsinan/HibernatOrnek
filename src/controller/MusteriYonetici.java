/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import model.Musteri;
import org.hibernate.Session;

/**
 *
 * @author asimsinanyuksel
 */
public class MusteriYonetici {

    private JTable musteriTablo;
    private final static String SORGU_KALIP = "from Musteri m";
    private Session session;
    private Vector<String> sutunlar = new Vector<>();
    private Vector<Object> satir;
    private DefaultTableModel model;

    public MusteriYonetici() {
        sutunlar.add("Müşteri ID");
        sutunlar.add("Ad");
        sutunlar.add("Soyad");
        sutunlar.add("Şehir");
        sutunlar.add("Tel No");
    }

    public void setModel() {
        model = (DefaultTableModel) musteriTablo.getModel();
        model.setColumnIdentifiers(sutunlar);
    }

    public void musteriGetir(String aranan, String filtre) {
        String sorguMetin = "";
        if (filtre.equalsIgnoreCase("ad")) {
            sorguMetin = SORGU_KALIP + " where m.ad like '%" + aranan + "%'";
        } else if (filtre.equalsIgnoreCase("adres")) {
            sorguMetin = SORGU_KALIP + " where m.adres like '%" + aranan + "%'";
        }
        session.beginTransaction();
        List musterilerList = session.createQuery(sorguMetin).list();
        session.getTransaction().commit();
        musteriGoster(musterilerList);

    }
    
   public void musteriEkle(Musteri musteri) {
        session.beginTransaction();
        session.save(musteri);
        session.getTransaction().commit();
    }
    public void musteriGuncelle(Musteri musteri) {
        session.beginTransaction();
        session.merge(musteri);
        session.getTransaction().commit();
    }
      public void musteriSil(int id) {
        session.beginTransaction();
        Musteri musteri= (Musteri)session.load(Musteri.class,id);
        session.delete(musteri);
        session.getTransaction().commit();
    }

    public void tumMusterileriGetir() {
        session.beginTransaction();
        List musterilerList = session.createQuery(SORGU_KALIP).list();
        session.getTransaction().commit();
        musteriGoster(musterilerList);
    }

    public void ac(JLabel durum) {
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                session = HibernateUtil.getSessionFactory().openSession();
                return null;
            }
            @Override
            protected void done() {
             durum.setForeground(Color.green);
             durum.setText("Bağlandı");
                
            }
            
        };
        worker.execute();
    }

    public void kapat() {
        session.close();
    }

    private void musteriGoster(List<Musteri> musterilerList) {
        model.getDataVector().removeAllElements();
        for (Musteri gelenMusteri : musterilerList) {
            satir = new Vector();
            satir.add(gelenMusteri.getMusteriid());
            satir.add(gelenMusteri.getAd());
            satir.add(gelenMusteri.getSoyad());
            satir.add(gelenMusteri.getAdres());
            satir.add(gelenMusteri.getTelno());
            model.addRow(satir);
        }
    }

    /**
     * @return the musteriTablo
     */
    public JTable getMusteriTablo() {
        return musteriTablo;
    }

    /**
     * @param musteriTablo the musteriTablo to set
     */
    public void setMusteriTablo(JTable musteriTablo) {
        this.musteriTablo = musteriTablo;
    }
}
