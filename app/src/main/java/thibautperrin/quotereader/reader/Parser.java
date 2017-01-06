package thibautperrin.quotereader.reader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import thibautperrin.quotereader.bean.Dtc;
import thibautperrin.quotereader.bean.Nsf;
import thibautperrin.quotereader.bean.Sentence;
import thibautperrin.quotereader.bean.Vdm;
import thibautperrin.quotereader.reader.parserException.NotExistingUrlException;
import thibautperrin.quotereader.reader.parserException.WebPageChangedException;

/**
 * This class contains functions allowing to parse web pages to extract VDM, DTC and NSF.
 */
class Parser {

    /**
     * This method goes on the page pageNumber and retrieves the VDMs on it.
     *
     * @param pageNumber a number upper or equals to zero.
     * @return A list containing the VDMs.
     */
    public static List<Vdm> getVdmPage(int pageNumber) throws NotExistingUrlException, WebPageChangedException {
        // Load the web page:
        Document doc;
        String url = "http://www.viedemerde.fr/?page=" + pageNumber;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException ex) {
            throw new NotExistingUrlException(url, ex);
        }

        // Extract VDMs:
        Elements bodies = doc.getElementsByTag("body");
        if (bodies.size() != 1) {
            throw new WebPageChangedException("VDM webpage changed.", url);
        } else {
            Element body = bodies.first();
            Elements panelBodies = body.getElementsByClass("panel-body");
            ArrayList<Vdm> list = new ArrayList<>(10);
            for (Element panelBody : panelBodies) {
                Elements children = panelBody.children();
                if (children.size() != 3) {
                    break;
                }
                if (children.get(0).className().equals("panel-content") &&
                        children.get(1).className().equals("panel-content")) {
                    Element panelContent = children.get(1);
                    Elements panelContentChildrens = panelContent.children();
                    if (panelContentChildrens.size() != 4) {
                        throw new WebPageChangedException("VDM webpage changed.", url);
                    }
                    Element p = panelContentChildrens.get(0);
                    if (!p.tagName().equals("p") || !p.className().equals("block")) {
                        throw new WebPageChangedException("VDM webpage changed.", url);
                    }
                    Elements pChildren = p.children();
                    if (pChildren.size() != 1) {
                        throw new WebPageChangedException("VDM webpage changed.", url);
                    }
                    Element a = pChildren.get(0);

                    List<TextNode> textNodes = a.textNodes();
                    if (textNodes.size() == 0) {
                        throw new WebPageChangedException("VDM webpage changed.", url);
                    }
                    String content = "";
                    for (TextNode textNode : textNodes) {
                        content += textNode.text();
                    }
                    if (!a.hasAttr("href")) {
                        throw new WebPageChangedException("VDM webpage changed.", url);
                    }
                    String endUrl = a.attr("href");
                    Vdm vdm = new Vdm(content, endUrl);
                    list.add(vdm);
                }
            }
            return list;
        }
    }

    /**
     * This method goes on the page pageNumber and retrieves the NSFs on it.
     *
     * @param pageNumber a number upper or equals to 1.
     * @return A list containing the NSFs.
     */
    public static List<Nsf> getNsfPage(int pageNumber) throws NotExistingUrlException, WebPageChangedException {
        // Load the web page:
        Document doc;
        String url = "http://nuitsansfolie.com/nsf/last?page=" + pageNumber;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException ex) {
            throw new NotExistingUrlException(url, ex);
        }

        // Extract NSFs:
        ArrayList<Nsf> list = new ArrayList<>();
        Elements articles = doc.getElementsByTag("article");
        for (Element article : articles) {
            int number;
            String content, date;

            // Retrieve the number:
            Attributes attributes = article.attributes();
            if (attributes.hasKey("id")) {
                String num = attributes.get("id");
                try {
                    number = Integer.parseInt(num);
                } catch (NumberFormatException e) {
                    throw new WebPageChangedException("NSF webpage changed.", url, e);
                }
            } else {
                throw new WebPageChangedException("NSF webpage changed.", url);
            }

            // Retrieve the date:
            Elements dates = article.getElementsByClass("wtf-date");
            if (dates.isEmpty()) {
                throw new WebPageChangedException("NSF webpage changed.", url);
            } else {
                Element dateEl = dates.first();
                date = dateEl.text();
            }

            // Retrieve the content:
            Elements anecdotes = article.getElementsByClass("anecdote-excerpt");
            if (anecdotes.isEmpty()) {
                throw new WebPageChangedException("NSF webpage changed.", url);
            } else {
                Element anecdote = anecdotes.first();
                Elements ps = anecdote.getElementsByTag("p");
                if (ps.isEmpty()) {
                    throw new WebPageChangedException("NSF webpage changed.", url);
                } else {
                    Element p = ps.first();
                    content = p.text();
                }
            }

            Nsf nsf = new Nsf(content, number, date);
            list.add(nsf);
        }
        return list;
    }

    /**
     * This method goes on the page pageNumber and retrieves the DTCs on it.
     *
     * @param pageNumber a number upper or equals to 1.
     * @return A list containing the DTCs.
     */
    public static List<Dtc> getDtcPage(int pageNumber) throws NotExistingUrlException, WebPageChangedException {
        // Load the web page:
        Document doc;
        String url = "http://danstonchat.com/latest/" + pageNumber + ".html";
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException ex) {
            throw new NotExistingUrlException(url, ex);
        }

        // Extract DTCs:
        ArrayList<Dtc> list = new ArrayList<>();
        try {
            Element element = doc.getElementsByClass("item-listing").get(0);
            Elements elements = element.getElementsByClass("item-content");
            for (Element p : elements) {
                Element a = p.child(0);
                String href = a.attributes().get("href");
                if (!href.startsWith("http://danstonchat.com/")) {
                    throw new WebPageChangedException("DTC webpage changed.", url);
                }
                String s = href.substring(23).split("[.]")[0];
                int number = Integer.parseInt(s);
                ArrayList<Sentence> sentences = new ArrayList<>();
                List<Node> nodes = a.childNodes();
                if (nodes.isEmpty()) {
                    throw new WebPageChangedException("DTC webpage changed.", url);
                }
                String author = null;
                for (Node node : nodes) {
                    if (node instanceof Element) {
                        Element el = (Element) node;
                        if (el.tagName().equals("span")) {
                            if (author != null) {
                                Sentence sentence = new Sentence(author, "");
                                sentences.add(sentence);
                            }
                            author = el.text();
                        }
                    } else if (node instanceof TextNode) {
                        String text = ((TextNode) node).text();
                        Sentence sentence = new Sentence(author, text);
                        author = null;
                        sentences.add(sentence);
                    }
                }
                Dtc dtc = new Dtc(sentences, number);
                list.add(dtc);
            }
            return list;
        } catch (IndexOutOfBoundsException ex) {
            throw new WebPageChangedException("DTC webpage changed.", url, ex);
        }
    }
}