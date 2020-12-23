package cn.jpzpers.bean;

import java.io.Serializable;
import java.util.HashMap;

//业务属性
public class UserStkAsset implements Serializable {

    public static final long serialVersionUID = -3010398486999758L;

    //新股中签	F100  //在途业务开发
    private HashMap<String, HashMap<String, String>> RealassetNewShares;
    //配股缴款	F200  //在途业务开发
    private HashMap<String,HashMap<String,String>> RealassetAllotmentPayment;
    //可转债中签  F300  //在途业务开发
    private HashMap<String,HashMap<String,String>> RealassetConvertibleBonds;
    //配债缴款	F400  //在途业务开发
    private HashMap<String,HashMap<String,String>> RealassetDebtPayment;
    //沪市分红送股	F500
    private HashMap<String,HashMap<String,String>> RealassetShangHaiStockDividends;

    //在途金额
    private HashMap<String,HashMap<String,String>> TransitAmount;
    //信用账户_资产明细_证券持仓_担保品持仓_沪深A股持仓_股票 D101
    private HashMap<String,HashMap<String,String>> CollateralPositionShSzAShareStock;
    //信用账户_资产明细_证券持仓_担保品持仓_沪深A股持仓_新三板 D102
    private HashMap<String,HashMap<String,String>> CollateralPositionShSzAShareNewThreeBoards;
    //信用账户_资产明细_证券持仓_担保品持仓_沪深A股持仓_创业板 D103
    private HashMap<String,HashMap<String,String>> CollateralPositionShSzAShareGem;
    ////信用账户_资产明细_证券持仓_担保品持仓_场内基金持仓_ETF	D111
    private HashMap<String,HashMap<String,String>> CollateralPositionFloorFundETF;
    //信用账户_资产明细_证券持仓_担保品持仓_场内基金持仓_LOF	D112
    private HashMap<String,HashMap<String,String>> CollateralPositionFloorFundLOF;
    //信用账户_资产明细_证券持仓_担保品持仓_场内基金持仓_投资基金	D113
    private HashMap<String,HashMap<String,String>> CollateralPositionFloorInvestment;
    //信用账户_资产明细_证券持仓_担保品持仓_场内基金持仓_专户基金	D114
    private HashMap<String,HashMap<String,String>> CollateralPositionFloorAccount;
    //信用账户_资产明细_证券持仓_担保品持仓_债券持仓_国债	D121
    private HashMap<String,HashMap<String,String>> CollateralPositionsNationalebt;
    //信用账户_资产明细_证券持仓_担保品持仓_债券持仓_企业债券 D122
    private HashMap<String,HashMap<String,String>> CollateralPositionsEnterprise;
    //信用账户_资产明细_证券持仓_担保品持仓_债券持仓_转换债券	D123
    private HashMap<String,HashMap<String,String>> CollateralPositionsConvertibleBonds;
    //信用账户_资产明细_证券持仓_担保品持仓_债券持仓_公司债	D124
    private HashMap<String,HashMap<String,String>> CollateralPositionsCorporateBonds;
    //信用账户_资产明细_证券持仓_担保品持仓_债券持仓_可交换公司债	 D125
    private HashMap<String,HashMap<String,String>> CollateralPositionsExchangeable;
    //信用账户_资产明细_证券持仓_担保品持仓_债券持仓_私募债券	D126
    private HashMap<String,HashMap<String,String>> CollateralPositionsPrivatePlacement;
    //信用账户_资产明细_证券持仓_担保品持仓_债券持仓_次级债券 	D127
    private HashMap<String,HashMap<String,String>> CollateralPositionsSubordinated;
    //信用账户_资产明细_证券持仓_非担保品持仓_沪深A股持仓_股票	D201
    private HashMap<String,HashMap<String,String>> NonCollateralPositionShSzAShareStock;
    //信用账户_资产明细_证券持仓_非担保品持仓_沪深A股持仓_新三板	D202
    private HashMap<String,HashMap<String,String>> NonCollateralPositionShSzAShareNewThreeBoards;
    //信用账户_资产明细_证券持仓_非担保品持仓_沪深A股持仓_创业板	D203
    private HashMap<String,HashMap<String,String>> NonCollateralPositionShSzAShareGem;
    //信用账户_资产明细_证券持仓_非担保品持仓_场内基金持仓_ETF	D211
    private HashMap<String,HashMap<String,String>> NonCollateralPositionFloorFundETF;
    //信用账户_资产明细_证券持仓_非担保品持仓_场内基金持仓_LOF	D212
    private HashMap<String,HashMap<String,String>> NonCollateralPositionFloorFundLOF;
    //信用账户_资产明细_证券持仓_非担保品持仓_场内基金持仓_投资基金 	D213
    private HashMap<String,HashMap<String,String>> NonCollateralPositionFloorInvestment;
    //信用账户_资产明细_证券持仓_非担保品持仓_场内基金持仓_专户基金	D214
    private HashMap<String,HashMap<String,String>> NonCollateralPositionFloorAccount;
    //信用账户_资产明细_证券持仓_非担保品持仓_债券持仓_国债	D221
    private HashMap<String,HashMap<String,String>> NonCollateralPositionsNationalebt;
    //信用账户_资产明细_证券持仓_非担保品持仓_债券持仓_企业债券	 D222
    private HashMap<String,HashMap<String,String>> NonCollateralPositionsEnterprise;
    //信用账户_资产明细_证券持仓_非担保品持仓_债券持仓_转换债券	 D223
    private HashMap<String,HashMap<String,String>> NonCollateralPositionConvertibleBonds;
    //信用账户_资产明细_证券持仓_非担保品持仓_债券持仓_公司债	 D224
    private HashMap<String,HashMap<String,String>> NonCollateralPositionsCorporateBonds;
    //信用账户_资产明细_证券持仓_非担保品持仓_债券持仓_可交换公司债 D225
    private HashMap<String,HashMap<String,String>> NonCollateralPositionsExchangeable;
    //信用账户_资产明细_证券持仓_非担保品持仓_债券持仓_私募债券	D226
    private HashMap<String,HashMap<String,String>> NonCollateralPositionsPrivatePlacement;
    //信用账户_资产明细_证券持仓_非担保品持仓_债券持仓_次级债券	 D227
    private HashMap<String,HashMap<String,String>> NonCollateralPositionsSubordinated;
    //融资利率(可选)	B138
    private HashMap<String,HashMap<String,String>> FinancingInterestRate;
    //融券利率(可选)	B139
    private HashMap<String,HashMap<String,String>> MarginInterestRate;
    //融资可用额度	C002
    //融券可用额度	C003
    //授信总额度	C004
    private HashMap<String,HashMap<String,String>> Quota;
    //维持担保比例 A001
    private HashMap<String,HashMap<String,String>> MaintainGuaranteeProportion;
    // 总资产  B001 //总负债  B002
    private HashMap<String,HashMap<String,String>> TotalAssetsTotalLiabilities;

    //信用账户_资产明细_证券持仓_科创板
    private HashMap<String,HashMap<String,String>> dboStkassetCeStarMarket;



    //信用账户_资产明细_合约_融资合约_沪深A股持仓_股票
    private HashMap<String, HashMap<String, String>> dboStkassetRzStock;
    //信用账户_资产明细_合约_融资合约_沪深A股持仓_新三板
    private HashMap<String, HashMap<String, String>> dboStkassetRzCtbs;
    //信用账户_资产明细_合约_融资合约_沪深A股持仓_创业板
    private HashMap<String, HashMap<String, String>> dboStkassetRzGem;
    //信用账户_资产明细_合约_融资合约_场内基金持仓_ETF
    private HashMap<String, HashMap<String, String>> dboStkassetRzFundETF;
    //信用账户_资产明细_合约_融资合约_场内基金持仓_LOF
    private HashMap<String, HashMap<String, String>> dboStkassetRzFundLOF;
    //信用账户_资产明细_合约_融资合约_场内基金持仓_投资基金
    private HashMap<String, HashMap<String, String>> dboStkassetRzFundInvest;
    //信用账户_资产明细_合约_融资合约_场内基金持仓_专户基金
    private HashMap<String, HashMap<String, String>> dboStkassetRzNomFundSpe;
    //信用账户_资产明细_合约_融资合约_债券持仓_国债
    private HashMap<String, HashMap<String, String>> dboStkassetRzNomBondCy;
    //信用账户_资产明细_合约_融资合约_债券持仓_企业债券
    private HashMap<String, HashMap<String, String>> dboStkassetRzBondEt;
    //信用账户_资产明细_合约_融资合约_债券持仓_转换债券
    private HashMap<String, HashMap<String, String>> dboStkassetRzNomBondCh;
    //信用账户_资产明细_合约_融资合约_债券持仓_公司债
    private HashMap<String, HashMap<String, String>> dboStkassetRzNomBondCm;
    //信用账户_资产明细_合约_融资合约_债券持仓_可交换公司债
    private HashMap<String, HashMap<String, String>> dboStkassetRzNomBondMaCh;
    //信用账户_资产明细_合约_融资合约_债券持仓_私募债券
    private HashMap<String, HashMap<String, String>> dboStkassetRzNomBondPa;
    //信用账户_资产明细_合约_融资合约_债券持仓_次级债券
    private HashMap<String, HashMap<String, String>> dboStkassetRzNomBondSec;

    //信用账户_资产明细_合约_融券合约_沪深A股持仓_股票
    private HashMap<String, HashMap<String, String>> dboStkassetRqStock;
    //信用账户_资产明细_合约_融券合约_沪深A股持仓_新三板
    private HashMap<String, HashMap<String, String>> dboStkassetRqCtbs;
    //信用账户_资产明细_合约_融券合约_沪深A股持仓_创业板
    private HashMap<String, HashMap<String, String>> dboStkassetRqGem;
    //信用账户_资产明细_合约_融券合约_场内基金持仓_ETF
    private HashMap<String, HashMap<String, String>> dboStkassetRqFundETF;
    //信用账户_资产明细_合约_融券合约_场内基金持仓_LOF
    private HashMap<String, HashMap<String, String>> dboStkassetRqFundLOF;
    //信用账户_资产明细_合约_融券合约_场内基金持仓_投资基金
    private HashMap<String, HashMap<String, String>> dboStkassetRqFundInvest;
    //信用账户_资产明细_合约_融券合约_场内基金持仓_专户基金
    private HashMap<String, HashMap<String, String>> dboStkassetRqNomFundSpe;
    //信用账户_资产明细_合约_融券合约_债券持仓_国债
    private HashMap<String, HashMap<String, String>> dboStkassetRqNomBondCy;
    //信用账户_资产明细_合约_融券合约_债券持仓_企业债券
    private HashMap<String, HashMap<String, String>> dboStkassetRqBondEt;
    //信用账户_资产明细_合约_融券合约_债券持仓_转换债券
    private HashMap<String, HashMap<String, String>> dboStkassetRqNomBondCh;
    //信用账户_资产明细_合约_融券合约_债券持仓_公司债
    private HashMap<String, HashMap<String, String>> dboStkassetRqNomBondCm;
    //信用账户_资产明细_合约_融券合约_债券持仓_可交换公司债
    private HashMap<String, HashMap<String, String>> dboStkassetRqNomBondMaCh;
    //信用账户_资产明细_合约_融券合约_债券持仓_私募债券
    private HashMap<String, HashMap<String, String>> dboStkassetRqNomBondPa;
    //信用账户_资产明细_合约_融券合约_债券持仓_次级债券
    private HashMap<String, HashMap<String, String>> dboStkassetRqNomBondSec;

    //信用账户_资产明细_合约_融资合约_科创版
    private HashMap<String, HashMap<String, String>> dboStkassetRzStarMarket;
    //信用账户_资产明细_合约_融券合约_科创版
    private HashMap<String, HashMap<String, String>> dboStkassetRqStarMarket;

    //信用账户_资金金额,可用资金,可取资金,冻结资金
    private HashMap<String,HashMap<String,String>>   dboFundassetCapital;
    //信用账户_新股冻结资金
    private HashMap<String,HashMap<String,String>>   dboMateconfirmNewFrozenCapital;

    //风险概况_维持担保比例
    private HashMap<String,HashMap<String,String>>   RiskdbRiskassetdebtRiskRatio;
    //总资产
    private HashMap<String,HashMap<String,String>>   RiskdbRiskassetdebtAllAsset;
    //总负债
    private HashMap<String,HashMap<String,String>>   RiskdbRiskassetdebtAllDebts;

    private HashMap<String,HashMap<String,String>>   RiskdbRiskassetdebtAllAssetAndAllDebts;

    //风险概况  信用状态
    private HashMap<String,HashMap<String,String>>   riskCreditStatus;

    //风险概况  风险概况_最底线 风险概况_警戒线 风险概况_平仓线

    private HashMap<String,HashMap<String,String>>   riskCreditLines;

    //额度_融资可用额 额度_融券可用额度 额度_授信总额度
    private HashMap<String,HashMap<String,String>>   fundcreditassetAmount;


    //普通账户
    //资产概况_现金类_RMB
    private HashMap<String,HashMap<String,String>>   generalAccountRmbCash;
    //资产明细_证券类_沪深_沪深A股_股票
    private HashMap<String, HashMap<String, String>> dboStkassetGenStock;
    //资产明细_证券类_沪深_沪深A股_新三板
    private HashMap<String, HashMap<String, String>> dboStkassetGenCtbs;
    //资产明细_证券类_沪深_沪深A股_科创板
    private HashMap<String, HashMap<String, String>> dboStkassetGenStarMarktet;
    //资产明细_证券类_沪深_沪深A股_创业板
    private HashMap<String, HashMap<String, String>> dboStkassetGenGem;
    //资产明细_证券类_沪深_场内基金_ETF
    private HashMap<String, HashMap<String, String>> dboStkassetGenFundETF;
    //资产明细_证券类_沪深_场内基金__LOF
    private HashMap<String, HashMap<String, String>> dboStkassetGenFundLOF;
    //资产明细_证券类_沪深_场内基金_投资基金
    private HashMap<String, HashMap<String, String>> dboStkassetGenFundInvest;
    //资产明细_证券类_沪深_场内基金_专户基金
    private HashMap<String, HashMap<String, String>> dboStkassetGenFundSpe;
    //资产明细_证券类_沪深_债券_国债
    private HashMap<String, HashMap<String, String>> dboStkassetGenBondCy;
    //资产明细_证券类_沪深_债券_企业债券
    private HashMap<String, HashMap<String, String>> dboStkassetGenBondEt;
    //资产明细_证券类_沪深_债券_转换债券
    private HashMap<String, HashMap<String, String>> dboStkassetGenBondCh;
    //资产明细_证券类_沪深_债券_公司债
    private HashMap<String, HashMap<String, String>> dboStkassetGenBondCm;
    //资产明细_证券类_沪深_债券_可交换公司债
    private HashMap<String, HashMap<String, String>> dboStkassetGenBondMaCh;
    //资产明细_证券类_沪深_债券_私募债券
    private HashMap<String, HashMap<String, String>> dboStkassetGenBondPa;
    //资产明细_证券类_沪深_债券_次级债券
    private HashMap<String, HashMap<String, String>> dboStkassetGenBondSec;
    //资产明细_证券类_股转
    private HashMap<String, HashMap<String, String>> dboStkassetGenShareDeal;
    //资产明细_证券类_港股通
    private HashMap<String, HashMap<String, String>> dboStkassetGenHkStock;

    //普通账户在途
    //在途金额
    private HashMap<String, HashMap<String, String>> genTransitAmount;
    // 新股中签
    private HashMap<String, HashMap<String, String>> genNewShares;
    //配债缴款
    private HashMap<String, HashMap<String, String>> genDebtPayment;
    //资产明细_在途(明细)_沪深A股_配股申购缴款股份未上账
    private HashMap<String, HashMap<String, String>> genAllotmentPayment;
    //可转债中签
    private HashMap<String, HashMap<String, String>> genConvertibleBonds;
    //沪股分红
    private HashMap<String, HashMap<String, String>> genShStockDividends;

    //沪市b股
    private HashMap<String, HashMap<String, String>> genShStockB;
    //资产明细_两网及退市B股(明细)展示
    private HashMap<String, HashMap<String, String>> genDelistShStockB;

    //深B股
    private HashMap<String, HashMap<String, String>> genSzStockB;

    //资产概况_现金类_HKD
    private HashMap<String,HashMap<String,String>>   generalAccountHkdCash;
    //资产概况_现金类_USD
    private HashMap<String,HashMap<String,String>>   generalAccountUsdCash;
    //资产明细_证券类_理财_国债逆回购
    private HashMap<String,HashMap<String,String>>   financialGznhg;
    //资产明细_证券类_理财_报价回购
    private HashMap<String,HashMap<String,String>>   financialBjhg;
    //资产明细_证券类_质押_国债正回购
    private HashMap<String,HashMap<String,String>>   pledgeGzzhg;
    //资产明细_现金类_冻结_场外买入冻结_资管产品(净值型)申购冻结
    private HashMap<String,HashMap<String,String>>   cashnetValueSgdj;
    //资产明细_理财类_活期_天天增
    private HashMap<String,HashMap<String,String>>   financialTtz;
    //资产明细_理财类_活期_天添利
    private HashMap<String,HashMap<String,String>>   financialTtl;
    //资产明细_理财（明细）_活期_菱信基金（申财宝）
    private HashMap<String,HashMap<String,String>>   financialLxjjscb;
    //资产明细_理财类_基金_区分普通委托持仓和定投委托持仓(货币型)
   // private HashMap<String,HashMap<String,String>> fundCurrency  ;

    //资产明细_理财类_定期(资管净值型)
    private HashMap<String,HashMap<String,String>>   financialZgNetValue;

    //资产明细_负债合约(明细)展示_股票质押
    private HashMap<String,HashMap<String,String>>    liabilityStockPledge;
    //资产明细_证券类_质押_股票质押
    private HashMap<String,HashMap<String,String>>    securitiesStockPledge;
    //资产明细——现金类——冻结——新股中签冻结
    private HashMap<String,HashMap<String,String>>    cashFrozenNgzq;

    //资产明细_理财类_定期(银行代销理财)
    private HashMap<String,HashMap<String,String>>    financialBankRegular ;
    //资产明细_理财类_定期(收益凭证)
    private HashMap<String,HashMap<String,String>>    financiaIncomeCertificateRegular;

    //资产明细_理财（明细）_货币基金
    private HashMap<String,HashMap<String,String>>    financiaMoneyFund;
    //资产明细_理财（明细）_基金_非货币基金
    private HashMap<String,HashMap<String,String>>    financiaNonMoneyFund;
    //资产明细_理财（明细）_定期_资管产品
    private HashMap<String,HashMap<String,String>>    financiaZgRegular;
    //资产明细_理财（明细）_私募
    private HashMap<String,HashMap<String,String>>    financiaPrivatePlacement;

    //资产明细_在途(明细)_场内基金_场内基金赎回确认份额扣除资金未上账
    private HashMap<String,HashMap<String,String>>  genTransitCnFloorFund;
    //资产明细_在途(明细)_场外基金_公募基金_场外公募基金赎回确认份额扣除资金未上账
    private HashMap<String,HashMap<String,String>> genTransitCwPublicOfferingFund;
    //资产明细_在途(明细)_场外基金_私募基金_场外私募基金赎回确认份额扣除资金未上账
    private HashMap<String,HashMap<String,String>> genTransitCwPrivateFund;
    //资产明细_在途(明细)_收益凭证_收益凭证认购缴款份额未上账
    private HashMap<String,HashMap<String,String>>  genTransitIncomeCertificate;
    //资产明细_在途(明细)_信托_信托认购确认缴款份额未上账
    private HashMap<String,HashMap<String,String>> genTransitTrustSubscription;

    //汇率
    private HashMap<String,HashMap<String,String>> exchangeRate;

    //限售
    private HashMap<String,HashMap<String,String>> restrictShStocks;
    private HashMap<String,HashMap<String,String>> restrictSzStocks;
    private HashMap<String,HashMap<String,String>> restrictGzStocks;

    public HashMap<String,HashMap<String,String>> getTotalAssetsTotalLiabilities(){
        return TotalAssetsTotalLiabilities;
    }
    public void setTotalAssetsTotalLiabilities(HashMap<String,HashMap<String,String>> TotalAssetsTotalLiabilities){
        this.TotalAssetsTotalLiabilities = TotalAssetsTotalLiabilities;
    }
    public HashMap<String,HashMap<String,String>> getMaintainGuaranteeProportion(){
        return MaintainGuaranteeProportion;
    }
    public void setMaintainGuaranteeProportion(HashMap<String,HashMap<String,String>> MaintainGuaranteeProportion){
        this.MaintainGuaranteeProportion = MaintainGuaranteeProportion;
    }
    public HashMap<String,HashMap<String,String>> getQuota(){
        return Quota;
    }
    public void setQuota(HashMap<String,HashMap<String,String>> Quota){
        this.Quota = Quota;
    }
    public HashMap<String,HashMap<String,String>> getMarginInterestRate(){
        return MarginInterestRate;
    }
    public void setMarginInterestRate(HashMap<String,HashMap<String,String>> MarginInterestRate){
        this.MarginInterestRate = MarginInterestRate;
    }
    public HashMap<String,HashMap<String,String>> getFinancingInterestRate(){
        return FinancingInterestRate;
    }
    public void setFinancingInterestRate(HashMap<String,HashMap<String,String>> FinancingInterestRate){
        this.FinancingInterestRate = FinancingInterestRate;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionsPrivatePlacement(){
        return NonCollateralPositionsPrivatePlacement;
    }
    public void setNonCollateralPositionsPrivatePlacement(HashMap<String,HashMap<String,String>> NonCollateralPositionsPrivatePlacement){
        this.NonCollateralPositionsPrivatePlacement = NonCollateralPositionsPrivatePlacement;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionsSubordinated(){
        return NonCollateralPositionsSubordinated;
    }
    public void setNonCollateralPositionsSubordinated(HashMap<String,HashMap<String,String>> NonCollateralPositionsSubordinated){
        this.NonCollateralPositionsSubordinated = NonCollateralPositionsSubordinated;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionsExchangeable(){
        return NonCollateralPositionsExchangeable;
    }
    public void setNonCollateralPositionsExchangeable(HashMap<String,HashMap<String,String>> NonCollateralPositionsExchangeable){
        this.NonCollateralPositionsExchangeable = NonCollateralPositionsExchangeable;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionsCorporateBonds(){
        return NonCollateralPositionsCorporateBonds;
    }
    public void setNonCollateralPositionsCorporateBonds(HashMap<String,HashMap<String,String>> NonCollateralPositionsCorporateBonds){
        this.NonCollateralPositionsCorporateBonds = NonCollateralPositionsCorporateBonds;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionConvertibleBonds(){
        return NonCollateralPositionConvertibleBonds;
    }
    public void setNonCollateralPositionConvertibleBonds(HashMap<String,HashMap<String,String>> NonCollateralPositionConvertibleBonds){
        this.NonCollateralPositionConvertibleBonds = NonCollateralPositionConvertibleBonds;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionsEnterprise(){
        return NonCollateralPositionsEnterprise;
    }
    public void setNonCollateralPositionsEnterprise(HashMap<String,HashMap<String,String>> NonCollateralPositionsEnterprise){
        this.NonCollateralPositionsEnterprise = NonCollateralPositionsEnterprise;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionsNationalebt(){
        return NonCollateralPositionsNationalebt;
    }
    public void setNonCollateralPositionsNationalebt(HashMap<String,HashMap<String,String>> NonCollateralPositionsNationalebt){
        this.NonCollateralPositionsNationalebt = NonCollateralPositionsNationalebt;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionFloorAccount(){
        return NonCollateralPositionFloorAccount;
    }
    public void setNonCollateralPositionFloorAccount(HashMap<String,HashMap<String,String>> NonCollateralPositionFloorAccount){
        this.NonCollateralPositionFloorAccount = NonCollateralPositionFloorAccount;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionFloorInvestment(){
        return NonCollateralPositionFloorInvestment;
    }
    public void setNonCollateralPositionFloorInvestment(HashMap<String,HashMap<String,String>> NonCollateralPositionFloorInvestment){
        this.NonCollateralPositionFloorInvestment = NonCollateralPositionFloorInvestment;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionFloorFundLOF(){
        return NonCollateralPositionFloorFundLOF;
    }
    public void setNonCollateralPositionFloorFundLOF(HashMap<String,HashMap<String,String>> NonCollateralPositionFloorFundLOF){
        this.NonCollateralPositionFloorFundLOF = NonCollateralPositionFloorFundLOF;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionFloorFundETF(){
        return NonCollateralPositionFloorFundETF;
    }
    public void setNonCollateralPositionFloorFundETF(HashMap<String,HashMap<String,String>> NonCollateralPositionFloorFundETF){
        this.NonCollateralPositionFloorFundETF = NonCollateralPositionFloorFundETF;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionShSzAShareGem(){
        return NonCollateralPositionShSzAShareGem;
    }
    public void setNonCollateralPositionShSzAShareGem(HashMap<String,HashMap<String,String>> NonCollateralPositionShSzAShareGem){
        this.NonCollateralPositionShSzAShareGem = NonCollateralPositionShSzAShareGem;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionShSzAShareNewThreeBoards(){
        return NonCollateralPositionShSzAShareNewThreeBoards;
    }
    public void setNonCollateralPositionShSzAShareNewThreeBoards(HashMap<String,HashMap<String,String>> NonCollateralPositionShSzAShareNewThreeBoards){
        this.NonCollateralPositionShSzAShareNewThreeBoards = NonCollateralPositionShSzAShareNewThreeBoards;
    }
    public HashMap<String,HashMap<String,String>> getNonCollateralPositionShSzAShareStock(){
        return NonCollateralPositionShSzAShareStock;
    }
    public void setNonCollateralPositionShSzAShareStock(HashMap<String,HashMap<String,String>> NonCollateralPositionShSzAShareStock){
        this.NonCollateralPositionShSzAShareStock = NonCollateralPositionShSzAShareStock;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionsSubordinated(){
        return CollateralPositionsSubordinated;
    }
    public void setCollateralPositionsSubordinated(HashMap<String,HashMap<String,String>> CollateralPositionsSubordinated){
        this.CollateralPositionsSubordinated = CollateralPositionsSubordinated;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionsPrivatePlacement(){
        return CollateralPositionsPrivatePlacement;
    }
    public void setCollateralPositionsPrivatePlacement(HashMap<String,HashMap<String,String>> CollateralPositionsPrivatePlacement){
        this.CollateralPositionsPrivatePlacement = CollateralPositionsPrivatePlacement;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionsExchangeable(){
        return CollateralPositionsExchangeable;
    }
    public void setCollateralPositionsExchangeable(HashMap<String,HashMap<String,String>> CollateralPositionsExchangeable){
        this.CollateralPositionsExchangeable = CollateralPositionsExchangeable;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionsCorporateBonds(){
        return CollateralPositionsCorporateBonds;
    }
    public void setCollateralPositionsCorporateBonds(HashMap<String,HashMap<String,String>> CollateralPositionsCorporateBonds){
        this.CollateralPositionsCorporateBonds = CollateralPositionsCorporateBonds;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionsConvertibleBonds(){
        return CollateralPositionsConvertibleBonds;
    }
    public void setCollateralPositionsConvertibleBonds(HashMap<String,HashMap<String,String>> CollateralPositionsConvertibleBonds){
        this.CollateralPositionsConvertibleBonds = CollateralPositionsConvertibleBonds;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionsEnterprise(){
        return CollateralPositionsEnterprise;
    }
    public void setCollateralPositionsEnterprise(HashMap<String,HashMap<String,String>> CollateralPositionsEnterprise){
        this.CollateralPositionsEnterprise = CollateralPositionsEnterprise;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionsNationalebt(){
        return CollateralPositionsNationalebt;
    }
    public void setCollateralPositionsNationalebt(HashMap<String,HashMap<String,String>> CollateralPositionsNationalebt){
        this.CollateralPositionsNationalebt = CollateralPositionsNationalebt;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionFloorAccount(){
        return CollateralPositionFloorAccount;
    }
    public void setCollateralPositionFloorAccount(HashMap<String,HashMap<String,String>> CollateralPositionFloorAccount){
        this.CollateralPositionFloorAccount = CollateralPositionFloorAccount;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionFloorInvestment(){
        return CollateralPositionFloorInvestment;
    }
    public void setCollateralPositionFloorInvestment(HashMap<String,HashMap<String,String>> CollateralPositionFloorInvestment){
        this.CollateralPositionFloorInvestment = CollateralPositionFloorInvestment;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionFloorFundLOF(){
        return CollateralPositionFloorFundLOF;
    }
    public void setCollateralPositionFloorFundLOF(HashMap<String,HashMap<String,String>> CollateralPositionFloorFundLOF){
        this.CollateralPositionFloorFundLOF = CollateralPositionFloorFundLOF;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionFloorFundETF(){
        return CollateralPositionFloorFundETF;
    }
    public void setCollateralPositionFloorFundETF(HashMap<String,HashMap<String,String>> CollateralPositionFloorFundETF){
        this.CollateralPositionFloorFundETF = CollateralPositionFloorFundETF;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionShSzAShareGem(){
        return CollateralPositionShSzAShareGem;
    }
    public void setCollateralPositionShSzAShareGem(HashMap<String,HashMap<String,String>> CollateralPositionShSzAShareGem){
        this.CollateralPositionShSzAShareGem = CollateralPositionShSzAShareGem;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionShSzAShareNewThreeBoards(){
        return CollateralPositionShSzAShareNewThreeBoards;
    }
    public void setCollateralPositionShSzAShareNewThreeBoards(HashMap<String,HashMap<String,String>> CollateralPositionShSzAShareNewThreeBoards){
        this.CollateralPositionShSzAShareNewThreeBoards = CollateralPositionShSzAShareNewThreeBoards;
    }
    public HashMap<String,HashMap<String,String>> getCollateralPositionShSzAShareStock(){
        return CollateralPositionShSzAShareStock;
    }
    public void setCollateralPositionShSzAShareStock(HashMap<String,HashMap<String,String>> CollateralPositionShSzAShare){
        this.CollateralPositionShSzAShareStock = CollateralPositionShSzAShare;
    }
    public HashMap<String, HashMap<String, String>> getRealassetNewShares() {
        return RealassetNewShares;
    }
    public void setRealassetNewShares(HashMap<String, HashMap<String, String>> RealassetNewShares) {
        this.RealassetNewShares = RealassetNewShares;
    }
    public HashMap<String,HashMap<String,String>> getRealassetAllotmentPayment(){
        return RealassetAllotmentPayment;
    }
    public void setRealassetAllotmentPayment(HashMap<String,HashMap<String,String>> RealassetAllotmentPayment){
        this.RealassetAllotmentPayment = RealassetAllotmentPayment;
    }
    public HashMap<String,HashMap<String,String>> getRealassetConvertibleBonds(){
        return RealassetConvertibleBonds;
    }
    public void setRealassetConvertibleBonds(HashMap<String,HashMap<String,String>> RealassetConvertibleBonds){
        this.RealassetConvertibleBonds = RealassetConvertibleBonds;
    }
    public HashMap<String,HashMap<String,String>> getRealassetDebtPayment(){
        return RealassetDebtPayment;
    }
    public void setRealassetDebtPayment(HashMap<String,HashMap<String,String>> RealassetDebtPayment){
        this.RealassetDebtPayment = RealassetDebtPayment;
    }
    public HashMap<String,HashMap<String,String>> getRealassetShangHaiStockDividends(){
        return RealassetShangHaiStockDividends;
    }
    public void setRealassetShangHaiStockDividends(HashMap<String,HashMap<String,String>> RealassetShangHaiStockDividends){
        this.RealassetShangHaiStockDividends = RealassetShangHaiStockDividends;
    }


    public HashMap<String, HashMap<String, String>> getDboStkassetRzStock() {
        return dboStkassetRzStock;
    }

    public void setDboStkassetRzStock(HashMap<String, HashMap<String, String>> dboStkassetRzStock) {
        this.dboStkassetRzStock = dboStkassetRzStock;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzCtbs() {
        return dboStkassetRzCtbs;
    }

    public void setDboStkassetRzCtbs(HashMap<String, HashMap<String, String>> dboStkassetRzCtbs) {
        this.dboStkassetRzCtbs = dboStkassetRzCtbs;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzGem() {
        return dboStkassetRzGem;
    }

    public void setDboStkassetRzGem(HashMap<String, HashMap<String, String>> dboStkassetRzGem) {
        this.dboStkassetRzGem = dboStkassetRzGem;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzFundETF() {
        return dboStkassetRzFundETF;
    }

    public void setDboStkassetRzFundETF(HashMap<String, HashMap<String, String>> dboStkassetRzFundETF) {
        this.dboStkassetRzFundETF = dboStkassetRzFundETF;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzFundLOF() {
        return dboStkassetRzFundLOF;
    }

    public void setDboStkassetRzFundLOF(HashMap<String, HashMap<String, String>> dboStkassetRzFundLOF) {
        this.dboStkassetRzFundLOF = dboStkassetRzFundLOF;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzFundInvest() {
        return dboStkassetRzFundInvest;
    }

    public void setDboStkassetRzFundInvest(HashMap<String, HashMap<String, String>> dboStkassetRzFundInvest) {
        this.dboStkassetRzFundInvest = dboStkassetRzFundInvest;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzNomFundSpe() {
        return dboStkassetRzNomFundSpe;
    }

    public void setDboStkassetRzNomFundSpe(HashMap<String, HashMap<String, String>> dboStkassetRzNomFundSpe) {
        this.dboStkassetRzNomFundSpe = dboStkassetRzNomFundSpe;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzNomBondCy() {
        return dboStkassetRzNomBondCy;
    }

    public void setDboStkassetRzNomBondCy(HashMap<String, HashMap<String, String>> dboStkassetRzNomBondCy) {
        this.dboStkassetRzNomBondCy = dboStkassetRzNomBondCy;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzBondEt() {
        return dboStkassetRzBondEt;
    }

    public void setDboStkassetRzBondEt(HashMap<String, HashMap<String, String>> dboStkassetRzBondEt) {
        this.dboStkassetRzBondEt = dboStkassetRzBondEt;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzNomBondCh() {
        return dboStkassetRzNomBondCh;
    }

    public void setDboStkassetRzNomBondCh(HashMap<String, HashMap<String, String>> dboStkassetRzNomBondCh) {
        this.dboStkassetRzNomBondCh = dboStkassetRzNomBondCh;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzNomBondCm() {
        return dboStkassetRzNomBondCm;
    }

    public void setDboStkassetRzNomBondCm(HashMap<String, HashMap<String, String>> dboStkassetRzNomBondCm) {
        this.dboStkassetRzNomBondCm = dboStkassetRzNomBondCm;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzNomBondMaCh() {
        return dboStkassetRzNomBondMaCh;
    }

    public void setDboStkassetRzNomBondMaCh(HashMap<String, HashMap<String, String>> dboStkassetRzNomBondMaCh) {
        this.dboStkassetRzNomBondMaCh = dboStkassetRzNomBondMaCh;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzNomBondPa() {
        return dboStkassetRzNomBondPa;
    }

    public void setDboStkassetRzNomBondPa(HashMap<String, HashMap<String, String>> dboStkassetRzNomBondPa) {
        this.dboStkassetRzNomBondPa = dboStkassetRzNomBondPa;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzNomBondSec() {
        return dboStkassetRzNomBondSec;
    }

    public void setDboStkassetRzNomBondSec(HashMap<String, HashMap<String, String>> dboStkassetRzNomBondSec) {
        this.dboStkassetRzNomBondSec = dboStkassetRzNomBondSec;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqStock() {
        return dboStkassetRqStock;
    }

    public void setDboStkassetRqStock(HashMap<String, HashMap<String, String>> dboStkassetRqStock) {
        this.dboStkassetRqStock = dboStkassetRqStock;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqCtbs() {
        return dboStkassetRqCtbs;
    }

    public void setDboStkassetRqCtbs(HashMap<String, HashMap<String, String>> dboStkassetRqCtbs) {
        this.dboStkassetRqCtbs = dboStkassetRqCtbs;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqGem() {
        return dboStkassetRqGem;
    }

    public void setDboStkassetRqGem(HashMap<String, HashMap<String, String>> dboStkassetRqGem) {
        this.dboStkassetRqGem = dboStkassetRqGem;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqFundETF() {
        return dboStkassetRqFundETF;
    }

    public void setDboStkassetRqFundETF(HashMap<String, HashMap<String, String>> dboStkassetRqFundETF) {
        this.dboStkassetRqFundETF = dboStkassetRqFundETF;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqFundLOF() {
        return dboStkassetRqFundLOF;
    }

    public void setDboStkassetRqFundLOF(HashMap<String, HashMap<String, String>> dboStkassetRqFundLOF) {
        this.dboStkassetRqFundLOF = dboStkassetRqFundLOF;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqFundInvest() {
        return dboStkassetRqFundInvest;
    }

    public void setDboStkassetRqFundInvest(HashMap<String, HashMap<String, String>> dboStkassetRqFundInvest) {
        this.dboStkassetRqFundInvest = dboStkassetRqFundInvest;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqNomFundSpe() {
        return dboStkassetRqNomFundSpe;
    }

    public void setDboStkassetRqNomFundSpe(HashMap<String, HashMap<String, String>> dboStkassetRqNomFundSpe) {
        this.dboStkassetRqNomFundSpe = dboStkassetRqNomFundSpe;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqNomBondCy() {
        return dboStkassetRqNomBondCy;
    }

    public void setDboStkassetRqNomBondCy(HashMap<String, HashMap<String, String>> dboStkassetRqNomBondCy) {
        this.dboStkassetRqNomBondCy = dboStkassetRqNomBondCy;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqBondEt() {
        return dboStkassetRqBondEt;
    }

    public void setDboStkassetRqBondEt(HashMap<String, HashMap<String, String>> dboStkassetRqBondEt) {
        this.dboStkassetRqBondEt = dboStkassetRqBondEt;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqNomBondCh() {
        return dboStkassetRqNomBondCh;
    }

    public void setDboStkassetRqNomBondCh(HashMap<String, HashMap<String, String>> dboStkassetRqNomBondCh) {
        this.dboStkassetRqNomBondCh = dboStkassetRqNomBondCh;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqNomBondCm() {
        return dboStkassetRqNomBondCm;
    }

    public void setDboStkassetRqNomBondCm(HashMap<String, HashMap<String, String>> dboStkassetRqNomBondCm) {
        this.dboStkassetRqNomBondCm = dboStkassetRqNomBondCm;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqNomBondMaCh() {
        return dboStkassetRqNomBondMaCh;
    }

    public void setDboStkassetRqNomBondMaCh(HashMap<String, HashMap<String, String>> dboStkassetRqNomBondMaCh) {
        this.dboStkassetRqNomBondMaCh = dboStkassetRqNomBondMaCh;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqNomBondPa() {
        return dboStkassetRqNomBondPa;
    }

    public void setDboStkassetRqNomBondPa(HashMap<String, HashMap<String, String>> dboStkassetRqNomBondPa) {
        this.dboStkassetRqNomBondPa = dboStkassetRqNomBondPa;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqNomBondSec() {
        return dboStkassetRqNomBondSec;
    }

    public void setDboStkassetRqNomBondSec(HashMap<String, HashMap<String, String>> dboStkassetRqNomBondSec) {
        this.dboStkassetRqNomBondSec = dboStkassetRqNomBondSec;
    }


    public HashMap<String, HashMap<String, String>> getDboFundassetCapital() {
        return dboFundassetCapital;
    }

    public void setDboFundassetCapital(HashMap<String, HashMap<String, String>> dboFundassetCapital) {
        this.dboFundassetCapital = dboFundassetCapital;
    }

    public HashMap<String, HashMap<String, String>> getDboMateconfirmNewFrozenCapital() {
        return dboMateconfirmNewFrozenCapital;
    }

    public void setDboMateconfirmNewFrozenCapital(HashMap<String, HashMap<String, String>> dboMateconfirmNewFrozenCapital) {
        this.dboMateconfirmNewFrozenCapital = dboMateconfirmNewFrozenCapital;
    }

    public HashMap<String, HashMap<String, String>> getRiskdbRiskassetdebtRiskRatio() {
        return RiskdbRiskassetdebtRiskRatio;
    }

    public void setRiskdbRiskassetdebtRiskRatio(HashMap<String, HashMap<String, String>> riskdbRiskassetdebtRiskRatio) {
        RiskdbRiskassetdebtRiskRatio = riskdbRiskassetdebtRiskRatio;
    }

    public HashMap<String, HashMap<String, String>> getRiskdbRiskassetdebtAllAsset() {
        return RiskdbRiskassetdebtAllAsset;
    }

    public void setRiskdbRiskassetdebtAllAsset(HashMap<String, HashMap<String, String>> riskdbRiskassetdebtAllAsset) {
        RiskdbRiskassetdebtAllAsset = riskdbRiskassetdebtAllAsset;
    }

    public HashMap<String, HashMap<String, String>> getRiskdbRiskassetdebtAllDebts() {
        return RiskdbRiskassetdebtAllDebts;
    }

    public void setRiskdbRiskassetdebtAllDebts(HashMap<String, HashMap<String, String>> riskdbRiskassetdebtAllDebts) {
        RiskdbRiskassetdebtAllDebts = riskdbRiskassetdebtAllDebts;
    }
    public HashMap<String, HashMap<String, String>> getRiskdbRiskassetdebtAllAssetAndAllDebts() {
        return RiskdbRiskassetdebtAllAssetAndAllDebts;
    }

    public void setRiskdbRiskassetdebtAllAssetAndAllDebts(HashMap<String, HashMap<String, String>> riskdbRiskassetdebtAllAssetAndAllDebts) {
        RiskdbRiskassetdebtAllAssetAndAllDebts = riskdbRiskassetdebtAllAssetAndAllDebts;
    }

    public HashMap<String, HashMap<String, String>> getRiskCreditStatus() {
        return riskCreditStatus;
    }

    public void setRiskCreditStatus(HashMap<String, HashMap<String, String>> riskCreditStatus) {
        this.riskCreditStatus = riskCreditStatus;
    }

    public HashMap<String, HashMap<String, String>> getRiskCreditLines() {
        return riskCreditLines;
    }

    public void setRiskCreditLines(HashMap<String, HashMap<String, String>> riskCreditLines) {
        this.riskCreditLines = riskCreditLines;
    }

    public HashMap<String, HashMap<String, String>> getFundcreditassetAmount() {
        return fundcreditassetAmount;
    }

    public void setFundcreditassetAmount(HashMap<String, HashMap<String, String>> fundcreditassetAmount) {
        this.fundcreditassetAmount = fundcreditassetAmount;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRzStarMarket() {
        return dboStkassetRzStarMarket;
    }

    public void setDboStkassetRzStarMarket(HashMap<String, HashMap<String, String>> dboStkassetRzStarMarket) {
        this.dboStkassetRzStarMarket = dboStkassetRzStarMarket;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetRqStarMarket() {
        return dboStkassetRqStarMarket;
    }

    public void setDboStkassetRqStarMarket(HashMap<String, HashMap<String, String>> dboStkassetRqStarMarket) {
        this.dboStkassetRqStarMarket = dboStkassetRqStarMarket;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetCeStarMarket() {
        return dboStkassetCeStarMarket;
    }

    public void setDboStkassetCeStarMarket(HashMap<String, HashMap<String, String>> dboStkassetCeStarMarket) {
        this.dboStkassetCeStarMarket = dboStkassetCeStarMarket;
    }

    public HashMap<String, HashMap<String, String>> getTransitAmount() {
        return TransitAmount;
    }

    public void setTransitAmount(HashMap<String, HashMap<String, String>> transitAmount) {
        TransitAmount = transitAmount;
    }

    public HashMap<String, HashMap<String, String>> getGeneralAccountRmbCash() {
        return generalAccountRmbCash;
    }

    public void setGeneralAccountRmbCash(HashMap<String, HashMap<String, String>> generalAccountRmbCash) {
        this.generalAccountRmbCash = generalAccountRmbCash;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenStock() {
        return dboStkassetGenStock;
    }

    public void setDboStkassetGenStock(HashMap<String, HashMap<String, String>> dboStkassetGenStock) {
        this.dboStkassetGenStock = dboStkassetGenStock;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenCtbs() {
        return dboStkassetGenCtbs;
    }

    public void setDboStkassetGenCtbs(HashMap<String, HashMap<String, String>> dboStkassetGenCtbs) {
        this.dboStkassetGenCtbs = dboStkassetGenCtbs;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenStarMarktet() {
        return dboStkassetGenStarMarktet;
    }

    public void setDboStkassetGenStarMarktet(HashMap<String, HashMap<String, String>> dboStkassetGenStarMarktet) {
        this.dboStkassetGenStarMarktet = dboStkassetGenStarMarktet;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenGem() {
        return dboStkassetGenGem;
    }

    public void setDboStkassetGenGem(HashMap<String, HashMap<String, String>> dboStkassetGenGem) {
        this.dboStkassetGenGem = dboStkassetGenGem;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenFundETF() {
        return dboStkassetGenFundETF;
    }

    public void setDboStkassetGenFundETF(HashMap<String, HashMap<String, String>> dboStkassetGenFundETF) {
        this.dboStkassetGenFundETF = dboStkassetGenFundETF;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenFundLOF() {
        return dboStkassetGenFundLOF;
    }

    public void setDboStkassetGenFundLOF(HashMap<String, HashMap<String, String>> dboStkassetGenFundLOF) {
        this.dboStkassetGenFundLOF = dboStkassetGenFundLOF;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenFundInvest() {
        return dboStkassetGenFundInvest;
    }

    public void setDboStkassetGenFundInvest(HashMap<String, HashMap<String, String>> dboStkassetGenFundInvest) {
        this.dboStkassetGenFundInvest = dboStkassetGenFundInvest;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenFundSpe() {
        return dboStkassetGenFundSpe;
    }

    public void setDboStkassetGenFundSpe(HashMap<String, HashMap<String, String>> dboStkassetGenFundSpe) {
        this.dboStkassetGenFundSpe = dboStkassetGenFundSpe;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenBondCy() {
        return dboStkassetGenBondCy;
    }

    public void setDboStkassetGenBondCy(HashMap<String, HashMap<String, String>> dboStkassetGenBondCy) {
        this.dboStkassetGenBondCy = dboStkassetGenBondCy;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenBondEt() {
        return dboStkassetGenBondEt;
    }

    public void setDboStkassetGenBondEt(HashMap<String, HashMap<String, String>> dboStkassetGenBondEt) {
        this.dboStkassetGenBondEt = dboStkassetGenBondEt;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenBondCh() {
        return dboStkassetGenBondCh;
    }

    public void setDboStkassetGenBondCh(HashMap<String, HashMap<String, String>> dboStkassetGenBondCh) {
        this.dboStkassetGenBondCh = dboStkassetGenBondCh;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenBondCm() {
        return dboStkassetGenBondCm;
    }

    public void setDboStkassetGenBondCm(HashMap<String, HashMap<String, String>> dboStkassetGenBondCm) {
        this.dboStkassetGenBondCm = dboStkassetGenBondCm;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenBondMaCh() {
        return dboStkassetGenBondMaCh;
    }

    public void setDboStkassetGenBondMaCh(HashMap<String, HashMap<String, String>> dboStkassetGenBondMaCh) {
        this.dboStkassetGenBondMaCh = dboStkassetGenBondMaCh;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenBondPa() {
        return dboStkassetGenBondPa;
    }

    public void setDboStkassetGenBondPa(HashMap<String, HashMap<String, String>> dboStkassetGenBondPa) {
        this.dboStkassetGenBondPa = dboStkassetGenBondPa;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenBondSec() {
        return dboStkassetGenBondSec;
    }

    public void setDboStkassetGenBondSec(HashMap<String, HashMap<String, String>> dboStkassetGenBondSec) {
        this.dboStkassetGenBondSec = dboStkassetGenBondSec;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenShareDeal() {
        return dboStkassetGenShareDeal;
    }

    public void setDboStkassetGenShareDeal(HashMap<String, HashMap<String, String>> dboStkassetGenShareDeal) {
        this.dboStkassetGenShareDeal = dboStkassetGenShareDeal;
    }

    public HashMap<String, HashMap<String, String>> getDboStkassetGenHkStock() {
        return dboStkassetGenHkStock;
    }

    public void setDboStkassetGenHkStock(HashMap<String, HashMap<String, String>> dboStkassetGenHkStock) {
        this.dboStkassetGenHkStock = dboStkassetGenHkStock;
    }

    public HashMap<String, HashMap<String, String>> getGenNewShares() {
        return genNewShares;
    }

    public void setGenNewShares(HashMap<String, HashMap<String, String>> genNewShares) {
        this.genNewShares = genNewShares;
    }

    public HashMap<String, HashMap<String, String>> getGenTransitAmount() {
        return genTransitAmount;
    }

    public void setGenTransitAmount(HashMap<String, HashMap<String, String>> genTransitAmount) {
        this.genTransitAmount = genTransitAmount;
    }

    public HashMap<String, HashMap<String, String>> getGenShStockDividends() {
        return genShStockDividends;
    }

    public void setGenShStockDividends(HashMap<String, HashMap<String, String>> genShStockDividends) {
        this.genShStockDividends = genShStockDividends;
    }

    public HashMap<String, HashMap<String, String>> getGenShStockB() {
        return genShStockB;
    }

    public void setGenShStockB(HashMap<String, HashMap<String, String>> genShStockB) {
        this.genShStockB = genShStockB;
    }

    public HashMap<String, HashMap<String, String>> getGenDelistShStockB() {
        return genDelistShStockB;
    }

    public void setGenDelistShStockB(HashMap<String, HashMap<String, String>> genDelistShStockB) {
        this.genDelistShStockB = genDelistShStockB;
    }

    public HashMap<String, HashMap<String, String>> getGenSzStockB() {
        return genSzStockB;
    }

    public void setGenSzStockB(HashMap<String, HashMap<String, String>> genSzStockB) {
        this.genSzStockB = genSzStockB;
    }

    public HashMap<String, HashMap<String, String>> getGeneralAccountHkdCash() {
        return generalAccountHkdCash;
    }

    public void setGeneralAccountHkdCash(HashMap<String, HashMap<String, String>> generalAccountHkdCash) {
        this.generalAccountHkdCash = generalAccountHkdCash;
    }

    public HashMap<String, HashMap<String, String>> getGeneralAccountUsdCash() {
        return generalAccountUsdCash;
    }

    public void setGeneralAccountUsdCash(HashMap<String, HashMap<String, String>> generalAccountUsdCash) {
        this.generalAccountUsdCash = generalAccountUsdCash;
    }
    public HashMap<String, HashMap<String, String>> getFinancialGznhg() {
        return financialGznhg;
    }

    public void setFinancialGznhg(HashMap<String, HashMap<String, String>> financialGznhg) {
        this.financialGznhg = financialGznhg;
    }

    public HashMap<String, HashMap<String, String>> getFinancialBjhg() {
        return financialBjhg;
    }

    public void setFinancialBjhg(HashMap<String, HashMap<String, String>> financialBjhg) {
        this.financialBjhg = financialBjhg;
    }

    public HashMap<String, HashMap<String, String>> getPledgeGzzhg() {
        return pledgeGzzhg;
    }

    public void setPledgeGzzhg(HashMap<String, HashMap<String, String>> pledgeGzzhg) {
        this.pledgeGzzhg = pledgeGzzhg;
    }

    public HashMap<String, HashMap<String, String>> getCashnetValueSgdj() {
        return cashnetValueSgdj;
    }

    public void setCashnetValueSgdj(HashMap<String, HashMap<String, String>> cashnetValueSgdj) {
        this.cashnetValueSgdj = cashnetValueSgdj;
    }

    public HashMap<String, HashMap<String, String>> getFinancialTtz() {
        return financialTtz;
    }

    public void setFinancialTtz(HashMap<String, HashMap<String, String>> financialTtz) {
        this.financialTtz = financialTtz;
    }

    public HashMap<String, HashMap<String, String>> getFinancialTtl() {
        return financialTtl;
    }

    public void setFinancialTtl(HashMap<String, HashMap<String, String>> financialTtl) {
        this.financialTtl = financialTtl;
    }

    public HashMap<String, HashMap<String, String>> getFinancialLxjjscb() {
        return financialLxjjscb;
    }

    public void setFinancialLxjjscb(HashMap<String, HashMap<String, String>> financialLxjjscb) {
        this.financialLxjjscb = financialLxjjscb;
    }

    /*public HashMap<String, HashMap<String, String>> getFundCurrency() {
        return fundCurrency;
    }

    public void setFundCurrency(HashMap<String, HashMap<String, String>> fundCurrency) {
        this.fundCurrency = fundCurrency;
    }
*/
    public HashMap<String, HashMap<String, String>> getFinancialZgNetValue() {
        return financialZgNetValue;
    }

    public void setFinancialZgNetValue(HashMap<String, HashMap<String, String>> financialZgNetValue) {
        this.financialZgNetValue = financialZgNetValue;
    }

    public HashMap<String, HashMap<String, String>> getLiabilityStockPledge() {
        return liabilityStockPledge;
    }

    public void setLiabilityStockPledge(HashMap<String, HashMap<String, String>> liabilityStockPledge) {
        this.liabilityStockPledge = liabilityStockPledge;
    }

    public HashMap<String, HashMap<String, String>> getSecuritiesStockPledge() {
        return securitiesStockPledge;
    }

    public void setSecuritiesStockPledge(HashMap<String, HashMap<String, String>> securitiesStockPledge) {
        this.securitiesStockPledge = securitiesStockPledge;
    }

    public HashMap<String, HashMap<String, String>> getCashFrozenNgzq() {
        return cashFrozenNgzq;
    }

    public void setCashFrozenNgzq(HashMap<String, HashMap<String, String>> cashFrozenNgzq) {
        this.cashFrozenNgzq = cashFrozenNgzq;
    }

    public HashMap<String, HashMap<String, String>> getFinancialBankRegular() {
        return financialBankRegular;
    }

    public void setFinancialBankRegular(HashMap<String, HashMap<String, String>> financialBankRegular) {
        this.financialBankRegular = financialBankRegular;
    }

    public HashMap<String, HashMap<String, String>> getFinanciaIncomeCertificateRegular() {
        return financiaIncomeCertificateRegular;
    }

    public void setFinanciaIncomeCertificateRegular(HashMap<String, HashMap<String, String>> financiaIncomeCertificateRegular) {
        this.financiaIncomeCertificateRegular = financiaIncomeCertificateRegular;
    }

    public HashMap<String, HashMap<String, String>> getGenDebtPayment() {
        return genDebtPayment;
    }

    public void setGenDebtPayment(HashMap<String, HashMap<String, String>> genDebtPayment) {
        this.genDebtPayment = genDebtPayment;
    }

    public HashMap<String, HashMap<String, String>> getGenAllotmentPayment() {
        return genAllotmentPayment;
    }

    public void setGenAllotmentPayment(HashMap<String, HashMap<String, String>> genAllotmentPayment) {
        this.genAllotmentPayment = genAllotmentPayment;
    }

    public HashMap<String, HashMap<String, String>> getGenConvertibleBonds() {
        return genConvertibleBonds;
    }

    public void setGenConvertibleBonds(HashMap<String, HashMap<String, String>> genConvertibleBonds) {
        this.genConvertibleBonds = genConvertibleBonds;
    }

    public HashMap<String, HashMap<String, String>> getFinanciaMoneyFund() {
        return financiaMoneyFund;
    }

    public void setFinanciaMoneyFund(HashMap<String, HashMap<String, String>> financiaMoneyFund) {
        this.financiaMoneyFund = financiaMoneyFund;
    }

    public HashMap<String, HashMap<String, String>> getFinanciaNonMoneyFund() {
        return financiaNonMoneyFund;
    }

    public void setFinanciaNonMoneyFund(HashMap<String, HashMap<String, String>> financiaNonMoneyFund) {
        this.financiaNonMoneyFund = financiaNonMoneyFund;
    }

    public HashMap<String, HashMap<String, String>> getFinanciaZgRegular() {
        return financiaZgRegular;
    }

    public void setFinanciaZgRegular(HashMap<String, HashMap<String, String>> financiaZgRegular) {
        this.financiaZgRegular = financiaZgRegular;
    }

    public HashMap<String, HashMap<String, String>> getFinanciaPrivatePlacement() {
        return financiaPrivatePlacement;
    }

    public void setFinanciaPrivatePlacement(HashMap<String, HashMap<String, String>> financiaPrivatePlacement) {
        this.financiaPrivatePlacement = financiaPrivatePlacement;
    }

    public HashMap<String, HashMap<String, String>> getGenTransitCnFloorFund() {
        return genTransitCnFloorFund;
    }

    public void setGenTransitCnFloorFund(HashMap<String, HashMap<String, String>> genTransitCnFloorFund) {
        this.genTransitCnFloorFund = genTransitCnFloorFund;
    }

    public HashMap<String, HashMap<String, String>> getGenTransitCwPublicOfferingFund() {
        return genTransitCwPublicOfferingFund;
    }

    public void setGenTransitCwPublicOfferingFund(HashMap<String, HashMap<String, String>> genTransitCwPublicOfferingFund) {
        this.genTransitCwPublicOfferingFund = genTransitCwPublicOfferingFund;
    }

    public HashMap<String, HashMap<String, String>> getGenTransitCwPrivateFund() {
        return genTransitCwPrivateFund;
    }

    public void setGenTransitCwPrivateFund(HashMap<String, HashMap<String, String>> genTransitCwPrivateFund) {
        this.genTransitCwPrivateFund = genTransitCwPrivateFund;
    }

    public HashMap<String, HashMap<String, String>> getGenTransitIncomeCertificate() {
        return genTransitIncomeCertificate;
    }

    public void setGenTransitIncomeCertificate(HashMap<String, HashMap<String, String>> genTransitIncomeCertificate) {
        this.genTransitIncomeCertificate = genTransitIncomeCertificate;
    }

    public HashMap<String, HashMap<String, String>> getGenTransitTrustSubscription() {
        return genTransitTrustSubscription;
    }

    public void setGenTransitTrustSubscription(HashMap<String, HashMap<String, String>> genTransitTrustSubscription) {
        this.genTransitTrustSubscription = genTransitTrustSubscription;
    }

    public HashMap<String, HashMap<String, String>> getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(HashMap<String, HashMap<String, String>> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public HashMap<String, HashMap<String, String>> getRestrictShStocks() {
        return restrictShStocks;
    }

    public void setRestrictShStocks(HashMap<String, HashMap<String, String>> restrictShStocks) {
        this.restrictShStocks = restrictShStocks;
    }

    public HashMap<String, HashMap<String, String>> getRestrictSzStocks() {
        return restrictSzStocks;
    }

    public void setRestrictSzStocks(HashMap<String, HashMap<String, String>> restrictSzStocks) {
        this.restrictSzStocks = restrictSzStocks;
    }

    public HashMap<String, HashMap<String, String>> getRestrictGzStocks() {
        return restrictGzStocks;
    }

    public void setRestrictGzStocks(HashMap<String, HashMap<String, String>> restrictGzStocks) {
        this.restrictGzStocks = restrictGzStocks;
    }
}