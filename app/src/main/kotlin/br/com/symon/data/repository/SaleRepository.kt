package br.com.symon.data.repository

import br.com.symon.base.BaseRepository
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest
import br.com.symon.data.model.requests.SendSaleRequest
import br.com.symon.data.webservice.SaleApiService
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaleRepository @Inject constructor(private val saleApiService: SaleApiService) : BaseRepository() {

    fun getSalesList(userToken: String, page: Int, pageSize: Int, radius: Int, latitude: Double, longitude: Double) =
            call(saleApiService.getSalesList(userToken, page, pageSize, radius, latitude, longitude))

    fun getSaleDetail(saleId: Int) = call((saleApiService.getSaleDetail(saleId)))
    fun uploadSale(userToken: String, sendSaleRequest: SendSaleRequest) = call(saleApiService.uploadSale(sendSaleRequest, userToken))
    fun uploadSalePhoto(userToken: String, saleId: Int, photo: MultipartBody.Part) = call(saleApiService.uploadSalePhoto(saleId, photo, userToken))
    fun updateSale(saleId: Int, userToken: String, sendSaleRequest: SendSaleRequest) = call((saleApiService.updateSale(saleId, sendSaleRequest, userToken)))
    fun deleteSale(saleId: Int) = call(saleApiService.deleteSale(saleId))

    fun likeSale(saleId: Int, userToken: String) = call(saleApiService.likeSale(saleId, userToken))
    fun disLikeSale(saleId: Int, userToken: String) = call(saleApiService.disLikeSale(saleId, userToken))

    fun searchSale(userToken: String, query: String, page: Int, pageSize: Int) = call(saleApiService.searchSale(userToken, query, page, pageSize))
    fun reportSale(saleReportRequest: SaleReportRequest?, userToken: String?) = call(saleApiService.reportSale(userToken, saleReportRequest))

    fun setFavorite(userToken: String, saleId: Int) = call(saleApiService.setFavorite(userToken, saleId))

    fun getComments(userToken: String, saleId: Int) = call(saleApiService.getComments(userToken, saleId))
    fun sendComment(userToken: String, saleId: Int, sendSaleCommentRequest: SendSaleCommentRequest) = call(saleApiService.sendComment(userToken, saleId, sendSaleCommentRequest))
}