package TradingPlatform;

import java.io.IOException;

import static TradingPlatform.ClientApp.networkManager;

/**
 * Creates a new instance of either a BUY or SELL order for a trade
 */
public class Trade{

    private boolean type;
    private AssetType asset;
    private int quantity;
    private int price;
    private int organisation;

    /**
     * Creates a new trade instance
     * @param type The trade type (true for buy, false for sell).
     * @param asset The trade asset.
     * @param quantity The quantity of the asset for the trade.
     * @param price The price of the asset for the trade.
     * @param organisationId The organisation initiating the trade order.
     */
    public Trade(boolean type, AssetType asset, int quantity, int price, int organisationId){
        this.type = type;
        this.asset = asset;
        this.quantity = quantity;
        this.price = price;
        this.organisation = organisationId;
    }

    /**
     * Adds a new Trade Order to the database for the values given
     * @param orgAssetId An int showing the organisation asset ID for the trade.
     * @param quantity An int showing the quantity of the asset for the trade.
     * @param type A boolean showing the trade type (true for buy, false for sell).
     * @param price An int showing the price of the asset for the trade.
     */
    public void addTradeOrder(int orgAssetId, int quantity, boolean type, int price) {
        try {
            networkManager.SendRequest("JDBCTradeDataSource", "addTradeOrder", new String[] {String.valueOf(orgAssetId), String.valueOf(quantity), String.valueOf(type), String.valueOf(price)});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sets the trade to cancelled
     * @param tradeID An int representing the current trade ID
     * @return A Boolean response from the server
     */
    public static Boolean setCancel(int tradeID) {
        Request response = null;
        try {
            response = networkManager.GetResponse("JDBCTradeDataSource", "setCancel", new String[] {String.valueOf(tradeID)});
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return response.getResponse();
    }

    /**
     * Gets the organisation asset ID for a given trade, returns -1 on error
     * @param tradeId An int representing the current trade ID
     * @return An int representing the organisation asset ID
     */
    public static int getOrganisationAssetId(int tradeId) {
        try {
            var response = NetworkManager.GetResponse("JDBCTradeDataSource", "getOrgAssetId", new String[] {String.valueOf(tradeId)});
            var args = response.getArguments();
            return Integer.parseInt(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

//    /**
//     * Returns the amount of credits needed for a trade
//     * @param asset The trade asset
//     * @param quantity The number of assets
//     * @return price
//     */
//    public float value(String asset, int quantity){
//        return 0;
//    }
//
//    /**
//     * Sets the type of a trade to either BUY or SELL
//     * @param type True for buy, false for sell.
//     */
//    public void setType(boolean type){
//    }
//
//    /**
//     * Gets the type of Trade, either BUY or SELL
//     * @return type
//     */
//    public String GetType(){
//        return "";
//    }
//
//    /**
//     * Sets the asset used for the trade
//     * @param asset The asset the trade will be for.
//     */
//    public void setAsset(AssetType asset){
//
//    }
//
//    /**
//     * Gets the asset from the trade
//     * @return asset
//     */
//    public AssetType getAsset(){
//        return null;
//    }
//
//    /**
//     * Sets the quantity of assets in the trade
//     * @param quantity The quantity of assets in the trade
//     */
//    public void setQuantity(int quantity){
//
//    }
//
//    /**
//     * Gets the quantity of assets in the trade
//     * @return quantity
//     */
//    public int getQuantity(){
//        return 0;
//    }
//
//    /**
//     * Returns the organisation linked to the current trade
//     * @return organisation
//     */
//    public String getOrganisation(){
//        return "";
//    }
}
