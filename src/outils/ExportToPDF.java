package outils;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import application.MainApp;

public class ExportToPDF {
	
	private static final String NEW_BASKERVILLE = "./ressources/fonts/NewBaskerville.otf";
	private static final String NEW_BASKERVILLE_ITALIC = "/ressources/fonts/NewBaskerville_Italic.ttf";
	private static final String NEW_BASKERVILLE_BOLD = "/ressources/fonts/NewBaskerville_Bold.ttf";
	private static final String NEW_BASKERVILLE_BOLD_ITALIC = "/ressources/fonts/NewBaskerville_BoldItalic.ttf";
	
	static class HeaderFooter extends PdfPageEventHelper {
		
		Font fontHF;
        /** Alternating phrase for the header. */
        Paragraph[] header = new Paragraph[2];
        /** Current page number (will be reset for every chapter). */
        int pagenumber = -1;
 
        public HeaderFooter() throws DocumentException, IOException{
        	fontHF = new Font(BaseFont.createFont(NEW_BASKERVILLE_ITALIC, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
        }
        
        /**
         * Initialize one of the headers.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Paragraph(MainApp.getProject().getName(), fontHF);
        }
 
        /**
         * Initialize one of the headers, based on the chapter title;
         * reset the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
         *      com.itextpdf.text.Paragraph)
         */
        public void onChapter(PdfWriter writer, Document document,
                float paragraphPosition, Paragraph title) {
            header[1] = new Paragraph(title.getContent().substring(2), fontHF);
            
        }
 
        /**
         * Increase the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
        }
 
        /**
         * Adds the header and the footer.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            if (pagenumber > 0){
            	switch(writer.getPageNumber() % 2) {
                case 0:
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_RIGHT, header[0],
                            rect.getRight(), rect.getTop(), 0);
                    break;
                case 1:
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_LEFT, header[1],
                            rect.getLeft(), rect.getTop(), 0);
                    break;
                }
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase(String.format("- %d -", pagenumber), fontHF),
                        (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
            }

            }
        }

 
	/**
	 * Crée un PDF du project à l'emplacement donné
	 * @param dest l'emplacement de destination
	 * @throws IOException
	 * @throws DocumentException
	 */
    public static void createPdf(String dest) throws IOException, DocumentException {
    	   	
    	//FONTS
    	BaseFont baseNewBaskerville = BaseFont.createFont(NEW_BASKERVILLE, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    	BaseFont baseNewBaskervilleBold = BaseFont.createFont(NEW_BASKERVILLE_BOLD, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    	BaseFont baseNewBaskervilleBoldItalic = BaseFont.createFont(NEW_BASKERVILLE_BOLD_ITALIC, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

    	
    	Font fontTitreLivre = new Font(baseNewBaskervilleBoldItalic,48);
    	Font fontTexteChap = new Font(baseNewBaskerville,14);
    	Font fontTitreChap = new Font(baseNewBaskervilleBold, 24);
    	
    	
    	
        Document document = new Document(PageSize.A4, 40, 40, 64, 64);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        
        Rectangle rect = new Rectangle(30, 30, 550, 800);
        writer.setBoxSize("art", rect);
        HeaderFooter event = new HeaderFooter();
        writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        writer.setPageEvent(event);
        document.open();
        Paragraph p = new Paragraph("\n\n\n" + MainApp.getProject().getName(), fontTitreLivre);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        
        for (int i = 1; i <= MainApp.getProject().getChapters().size(); i++) {
        	Paragraph title = new Paragraph(new Chunk(MainApp.getProject().getChapter(i-1).getTitle(), fontTitreChap));
        	LineSeparator dottedline = new LineSeparator();
            dottedline.setOffset(-13);
            title.add(dottedline);
            
        	Chapter chapter = new Chapter(title, i);
        	chapter.add(Chunk.NEWLINE);
        	    
          	//String[] tabParagraph = MainApp.getProject().getChapter(i-1).getText().split(System.lineSeparator());
        	String[] tabParagraph = MainApp.getProject().getChapter(i-1).getText().split("\n");
     	
        	for (String stringParagraph : tabParagraph) {
        		Paragraph paragraph = new Paragraph(stringParagraph, fontTexteChap);
        		
        		//Jamais d'alinea à la première ligne les bros !
        		paragraph.setSpacingAfter(10);
        	
        		paragraph.setFirstLineIndent(10);
        		
        		paragraph.setAlignment(Element.ALIGN_JUSTIFIED);         	

            	chapter.add(paragraph);
			}
            document.add(chapter);
		}      
        document.close();
    }
}