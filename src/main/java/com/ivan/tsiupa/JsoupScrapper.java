package com.ivan.tsiupa;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

/**
 *  This program scraping data
 *  from https://allegro.pl
 *
 *  @author Tsiupa Ivan
 *  @version 1.0
 *  @since  2021-03-31
 */
public class JsoupScrapper {

    private final String GLOBAL = "https://allegro.pl";

    private final String KIND = "https://allegro.pl/strefaokazji/smartokazje";
    private final String ClASS_KIND = "_1yyhi _b73bd_2C-E_ _b73bd_2_kir _b73bd_1wsaK mwdn_0";
    private final String ClASS_KIND_TITLE = "_b73bd_RBVvr _1h7wt _9hx3e";
    private final String[] ClASS_KIND_HREF = {
                                            "mpof_ki mr3m_1 mh85_56 myre_8v m389_6m mg9e_8 mvrt_8 mj7a_8 mh36_8 mzmg_7i _b73bd_NSNTK _b73bd_Da3Xr _b73bd_2GKHl _b73bd_q59yi _b73bd_1W1Sd mwfq_6m carousel-item",
                                            "mpof_ki mr3m_1 mh85_56 myre_8v m389_6m mg9e_8 mvrt_8 mj7a_8 mh36_8 mzmg_7i _b73bd_NSNTK _b73bd_Da3Xr _b73bd_2GKHl _b73bd_q59yi _b73bd_2x5-f _b73bd_1W1Sd mwfq_6m carousel-item"
    };

    private final String[] TYPE_BLOCKS = {
                                            "mpof_ki mqen_m6 mp7g_oh mh36_0 mvrt_0 mg9e_8 mj7a_8 m7er_k4 _1y62o _9c44d_1I1gg ",
                                            "mpof_ki mqen_m6 mp7g_oh mh36_0 mvrt_0 mg9e_8 mj7a_8 m7er_k4 _1y62o _9c44d_1I1gg _9c44d_UaoWo"
    };

    private final String TITLE = "mgn2_14 m9qz_yp mqu1_16 mp4t_0 m3h2_0 mryx_0 munh_0";
    private final String PRICE = "_1svub _lf05o";
    private final String DISCOUNT = "_9c44d_1uHr2";
    private final String SELLER = "mpof_ki m389_6m msa3_z4 mgn2_13";
    private final String STATE = "mpof_z0 m7er_k4";
    private final String QUANTITY_SOLD = "mpof_ki m389_6m munh_56_l";
    private final String DEPARTURE = "mp0t_ji mpof_vs _9c44d_1VS-Y      _9c44d_3px8G";
    private final String DELIVERY_PRICE = "_9c44d_1xKGX";

    public static void main(String[] args) {
            JsoupScrapper jsoupScrapper = new JsoupScrapper();

            ArrayList<String> listKind = new ArrayList<String>();
            ArrayList<Product> listEnd = new ArrayList<Product>();
            ArrayList<Product> list;

            jsoupScrapper.kind(listKind);

            int g = 0;
            for (String string: listKind){
                g++;
                if( g%2 != 0 )
                    System.out.printf("%-15s%s%s%n", string, "--------  " ,(g/2 + 1));
            }
        System.out.println(g);
            for(int i = 0; i<3; i++) {
                list = new ArrayList<>();

                int k;
                Scanner scanner = new Scanner(System.in);

                while (true){
                    System.out.print("Enter code of kind: ");
                    k = scanner.nextInt();
                    if(k > 0 && k < (g/2 + 1)) break;
                    else System.out.println("Input is incorrect");
                }

                jsoupScrapper.search(list, listKind.get((k * 2 - 1)), 1);

                listEnd.addAll(list);
                list = null;

                try(Writer writer = new FileWriter("product.csv")) {
                    StatefulBeanToCsv<Product> statefulBeanToCsv = new StatefulBeanToCsvBuilder<Product>(writer).build();
                    statefulBeanToCsv.write(listEnd);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CsvRequiredFieldEmptyException e) {
                    e.printStackTrace();
                } catch (CsvDataTypeMismatchException e) {
                    e.printStackTrace();
                }
            }
    }

        public ArrayList kind(ArrayList list) {
            Document document;

            try {
               document = Jsoup.connect(KIND).timeout(12000).get();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Elements repositoriesQ = document.getElementsByClass(ClASS_KIND);
            for (Element repositoryQ : repositoriesQ) {

                list.add(repositoryQ.getElementsByClass(ClASS_KIND_TITLE).text());
                list.add(
                        !repositoryQ.getElementsByClass(ClASS_KIND_HREF[0]).attr("href").equals("") ?
                        repositoryQ.getElementsByClass(ClASS_KIND_HREF[0]).attr("href") :
                        repositoryQ.getElementsByClass(ClASS_KIND_HREF[1]).attr("href")
                );
            }
            return list;
        }

        public ArrayList search(ArrayList list, String url, int page){
            Document doc;
                try {
                    doc = Jsoup.connect(GLOBAL + url + "&p="+ page).timeout(12000).get();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            for (String block: TYPE_BLOCKS) {
                Elements repositories = doc.getElementsByClass(block);
                for (Element repository : repositories) {
                    if (repository.getElementsByClass(DISCOUNT).text().contains("-")) {
                        if (list.size() < 100) {
                            list.add(
                                    new Product(
                                            repository.getElementsByClass(TITLE).text(),
                                            repository.getElementsByClass(PRICE).text(),
                                            repository.getElementsByClass(DISCOUNT).text(),
                                            repository.getElementsByClass(SELLER).text(),
                                            repository.getElementsByClass(STATE).text(),
                                            repository.getElementsByClass(QUANTITY_SOLD).text(),
                                            repository.getElementsByClass(DEPARTURE).text(),
                                            repository.getElementsByClass(DELIVERY_PRICE).text()
                                    )
                            );
                        } else return list;
                    }
                }
            }
            page++;
            search(list, url, page);
            return list;
        }
    }

