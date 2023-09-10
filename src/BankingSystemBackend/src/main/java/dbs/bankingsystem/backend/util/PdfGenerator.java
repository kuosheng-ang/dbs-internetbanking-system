package dbs.bankingsystem.backend.util;

import dbs.bankingsystem.backend.entity.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dbs.bankingsystem.backend.service.AccountService;
import dbs.bankingsystem.backend.service.TransactionService;
import dbs.bankingsystem.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Component
public class PdfGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGenerator.class);
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    public void generateAccounts(User user, String filePath){
        LOGGER.info("generateUserAccount()");
        User userProfile = userService.getUserFromEmail(user.getEmail());
        Account userAccount = accountService.getAccountByUsername(userProfile.getUsername());
        Transaction userAccountTransaction = transactionService.getTransactionByAccount(userAccount.getAccountNo());
        Document document=new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.addTitle("Here is your Account Balance");
            document.add(generateTable(userAccount, userAccountTransaction));
            document.close();

        } catch (FileNotFoundException | DocumentException ex){

            LOGGER.error("Exception in user account balance " +ex);
            ex.printStackTrace();
        }
    }
    public PdfPTable generateTable(Account account, Transaction transaction){
        PdfPTable table=new PdfPTable(2);

        PdfPCell cell = new PdfPCell(new Phrase("Account Balance"));

        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Account Holder - Summary"));
        cell.setColspan(2);
        table.addCell(cell);

        table.addCell("Account Holder First Name");
        table.addCell(account.getUser().getFirstName());

        table.addCell("Account Holder First Name");
        table.addCell(account.getUser().getLastName());

        table.addCell("Account Num");
        table.addCell(account.getAccountNo());

        table.addCell("Account Balance");
        table.addCell(account.getAccountBalance().toString());

        cell = new PdfPCell(new Phrase("Transaction Details"));
        cell.setColspan(2);
        table.addCell(cell);

        table.addCell("Transaction Date Time :");
        table.addCell(transaction.getTxDateTime().toString());

        table.addCell("Transaction From : ");
        table.addCell(transaction.getTransferFrom());

        table.addCell("Transaction From :");
        table.addCell(transaction.getTransferTo());

        table.addCell("Transaction Amount ($) :");
        table.addCell(transaction.getTxAmount().toString());

        return table;
    }
}
