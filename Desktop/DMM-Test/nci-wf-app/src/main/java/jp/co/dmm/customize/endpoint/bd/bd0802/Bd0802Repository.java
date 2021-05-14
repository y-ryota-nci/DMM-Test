package jp.co.dmm.customize.endpoint.bd.bd0802;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.bd.bd0801.Bd0801Entity;
import jp.co.dmm.customize.endpoint.bd.bd0801.Bd0801Repository;
import jp.co.dmm.customize.endpoint.bd.bd0801.Bd0801SearchRequest;
import jp.co.dmm.customize.jpa.entity.mw.BgtPlnHst;
import jp.co.dmm.customize.jpa.entity.mw.BgtPlnHstPK;
import jp.co.dmm.customize.jpa.entity.mw.BgtPlnHstver;
import jp.co.dmm.customize.jpa.entity.mw.BgtPlnHstverPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 予算計画履歴作成画面のリポジトリ
 */
@ApplicationScoped
public class Bd0802Repository extends BaseRepository {
	@Inject private SessionHolder sessionHolder;
	@Inject private Bd0801Repository bd0801;

	/** 予算履歴バージョンのインサート  */
	public BgtPlnHstver insertBgtPlnHstver(Bd0802CreateRequest req) {
		final int hstVersion = nextHstVersion(req);
		final BgtPlnHstverPK id = new BgtPlnHstverPK();
		id.setCompanyCd(req.companyCd);
		id.setYrCd(req.yrCd);
		id.setOrganizationCode(req.organizationCodeLv3);
		id.setRcvinspPayTp(req.rcvCostPayTp);
		id.setHstVersion(hstVersion);

		final BgtPlnHstver e = new BgtPlnHstver();
		e.setId(id);
		e.setHstDt(timestamp());
		e.setHstNm(req.hstNm);
		e.setHstRmk(req.hstRmk);
		e.setDltFg(DeleteFlag.OFF);

		final LoginInfo login = sessionHolder.getLoginInfo();
		e.setCorporationCodeCreated(login.getCorporationCode());
		e.setUserCodeCreated(login.getUserCode());
		e.setIpCreated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampCreated(timestamp());
		e.setCorporationCodeUpdated(login.getCorporationCode());
		e.setUserCodeUpdated(login.getUserCode());
		e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampUpdated(timestamp());

		em.persist(e);

		return e;
	}

	/** 履歴バージョンを採番 */
	private int nextHstVersion(Bd0802CreateRequest req) {
		final Object[] params = {
				req.companyCd, req.yrCd, req.organizationCodeLv3, req.rcvCostPayTp
		};
		final String sql = getSql("BD0802_01");
		return count(sql, params) + 1;
	}

	/** 予算履歴をインサート */
	public void insertBgtPlnHst(Bd0802CreateRequest req, BgtPlnHstver ver) {
		// 現在の予算計画を抽出
		final List<Bd0801Entity> srcList = getBgtPln(req);
		for (Bd0801Entity src : srcList) {
			final BgtPlnHstPK id = new BgtPlnHstPK();
			id.setCompanyCd(req.companyCd);
			id.setYrCd(req.yrCd);
			id.setOrganizationCode(req.organizationCodeLv3);
			id.setRcvinspPayTp(req.rcvCostPayTp);
			id.setHstVersion(ver.getId().getHstVersion());
			id.setBgtItmCd(src.bgtItmCd);

			final BgtPlnHst e = new BgtPlnHst();
			e.setId(id);
			e.setBgtAmt03(src.bgtAmt03);
			e.setBgtAmt04(src.bgtAmt04);
			e.setBgtAmt05(src.bgtAmt05);
			e.setBgtAmt06(src.bgtAmt06);
			e.setBgtAmt07(src.bgtAmt07);
			e.setBgtAmt08(src.bgtAmt08);
			e.setBgtAmt09(src.bgtAmt09);
			e.setBgtAmt10(src.bgtAmt10);
			e.setBgtAmt11(src.bgtAmt11);
			e.setBgtAmt12(src.bgtAmt12);
			e.setBgtAmt01(src.bgtAmt01);
			e.setBgtAmt02(src.bgtAmt02);
			e.setDltFg(DeleteFlag.OFF);

			final LoginInfo login = sessionHolder.getLoginInfo();
			e.setCorporationCodeCreated(login.getCorporationCode());
			e.setUserCodeCreated(login.getUserCode());
			e.setIpCreated(sessionHolder.getWfUserRole().getIpAddress());
			e.setTimestampCreated(timestamp());
			e.setCorporationCodeUpdated(login.getCorporationCode());
			e.setUserCodeUpdated(login.getUserCode());
			e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
			e.setTimestampUpdated(timestamp());

			em.persist(e);
		}
	}

	/** 現在の予算計画を抽出 */
	public List<Bd0801Entity> getBgtPln(Bd0802InitRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Bd0801SearchRequest cond = new Bd0801SearchRequest();
		cond.yrCd = req.yrCd;
		cond.organizationCodeLv3 = req.organizationCodeLv3;
		cond.rcvCostPayTp = req.rcvCostPayTp;
		cond.bsplTp = req.bsplTp;
		cond.companyCd = login.getCorporationCode();

		final List<Bd0801Entity> bd0801EntityList = bd0801.select(cond);
		for (Bd0801Entity bd0801Entity : bd0801EntityList) {
			if("1".equals(req.bsplTp)) {
				bd0801Entity.bgtAmtTtl = bd0801Entity.bgtAmt12;
			} else {

				BigDecimal bgtAmtTtl = new BigDecimal(0);
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt01) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt02) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt03) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt04) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt05) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt06) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt07) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt08) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt09) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt10) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt11) ;
				bgtAmtTtl = bgtAmtTtl.add(bd0801Entity.bgtAmt12) ;
				bd0801Entity.bgtAmtTtl = bgtAmtTtl;
			}
		}
		return bd0801EntityList;
	}
}
