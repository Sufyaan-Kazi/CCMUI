package com.pivotal.fieldengineering.sampledata.webapp;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.pivotal.fieldengineering.sampledata.beans.Account;
import com.pivotal.fieldengineering.sampledata.beans.Contact;
import com.pivotal.fieldengineering.sampledata.beans.Statement;
import com.pivotal.fieldengineering.sampledata.beans.Transaction;
import com.pivotal.fieldengineering.sampledata.data.RedisDAOImpl;
import com.pivotal.fieldengineering.sampledata.utils.Logger;
import com.pivotal.fieldengineering.sampledata.utils.StaticData;

@Controller
// @SessionAttributes("acc")
public class ViewController {
	@Autowired
	private RedisDAOImpl redisDAO = null;
	
	@Autowired
	private Environment env;
	private String ccmdsService = null;

	public ViewController() {
		super();
	}

	// @RequestMapping(value="/getStatementListForAcc",
	// method=RequestMethod.GET)
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStatementListForAcc(Model model) {
		Logger.INSTANCE.log("Forwarding to Thymeleaf!");
		model.addAttribute("account", new Account());
		
		if(ccmdsService == null){
			ccmdsService = env.getProperty("vcap.services.ccmds-service.credentials.uri");
		}
		return "login";
	}

	@RequestMapping(value = "/getStatementListForAcc", method = RequestMethod.POST)
	// public String getStatementListForAcc(@RequestParam(value="id",
	// required=false, defaultValue="146885") int accId, Model model){
	public String getStatementListForAcc(@ModelAttribute Account acc, Model model, HttpServletRequest request, HttpSession session) {
		Logger.INSTANCE.log("Fetching Data for: " + acc.getId());
		Logger.INSTANCE.log(model.toString());

		Logger.INSTANCE.log("About to call Spring Rest Service: " + StaticData.INSTANCE.CF_DOMAIN);
		RestTemplate restTemplate = new RestTemplate();
		Logger.INSTANCE.log(System.getenv("VCAP_SERVICES"));
		Logger.INSTANCE.log("Stuff: " + ccmdsService);
		acc = restTemplate.getForObject(ccmdsService + "/getAccount?accId=" + acc.getId(), Account.class);
		Logger.INSTANCE.log("Called Spring Rest Service: " + acc.toString());
		// acc = accDao.getAccountDetails(acc.getId());
		model.addAttribute("account", acc);

		// Logger.INSTANCE.log(StaticData.INSTANCE.env.getProperty("CF_DOMAIN"));
		Contact contRecord = restTemplate.getForObject(ccmdsService + "/getContactForAccount?accId=" + acc.getId(), Contact.class);
		// Logger.INSTANCE.log("Called Spring Rest Service: " + accDetails);
		Logger.INSTANCE.log("Called Spring Rest Service: " + contRecord.getFirst_name() + " " + contRecord.getLast_name());

		// Contact contRecord = accDao.getContactForAccount(acc.getId());
		model.addAttribute("contact", contRecord);

		Statement[] forNow = restTemplate.getForObject(ccmdsService + "/getStatementsForAccount?accId=" + acc.getId(), Statement[].class);
		List<Statement> stmnts = Arrays.asList(forNow);

		// List<Statement> stmnts = accDao.getStatementsForAccount(acc.getId());
		model.addAttribute("statementcount", stmnts.size());
		model.addAttribute("statements", stmnts);

		Logger.INSTANCE.log("Found: " + contRecord.getFirst_name() + " with " + stmnts.size() + " statements.");
		for (Statement stmnt : stmnts) {
			Logger.INSTANCE.log("\t" + stmnt.toString());
		}
		redisDAO.setValue("AccountNo", Integer.toString(acc.getId()));

		return "viewStatementsForAccount";
	}

	@RequestMapping(value = "/getTransactionsForStatement", method = RequestMethod.POST)
	public String getTransactionsForStatement(@ModelAttribute Statement statement, Model model, HttpServletRequest request, HttpSession session) {

		int accNo = Integer.parseInt(redisDAO.getValue("AccountNo"));
		RestTemplate restTemplate = new RestTemplate();

		// Change this to read/write JSON from Redis
		Statement[] forNow = restTemplate.getForObject(ccmdsService + "/getStatementsForAccount?accId=" + accNo, Statement[].class);
		List<Statement> stmnts = Arrays.asList(forNow);
		model.addAttribute("statement", stmnts.get(0));

		Logger.INSTANCE.log("Fetching transactions ......");
		Transaction[] forNowT = restTemplate.getForObject(ccmdsService + "/getTransactionsForStatement?accId=" + accNo + "&statementId=" + statement.getStatementId(),
				Transaction[].class);
		List<Transaction> transactions = Arrays.asList(forNowT);
		Logger.INSTANCE.log("\t" + transactions);
		// List<Transaction> transactions =
		// accDao.getTransactionsForStatement(accNo,
		// statement.getStatementId());

		Logger.INSTANCE.log("Found [" + transactions.size() + "] Transactions for Acc/Statement: " + accNo + "/" + statement.getStatementId());
		for (Transaction trans : transactions) {
			Logger.INSTANCE.log("\t" + trans.toString());
		}
		model.addAttribute("transactions", transactions);

		return "showStatementTransactions";
	}

}
