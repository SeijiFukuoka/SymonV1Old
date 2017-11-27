package br.com.symon.data.repository

import br.com.symon.base.BaseRepository
import br.com.symon.data.model.Sale
import br.com.symon.data.webservice.SaleApiService
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaleRepository @Inject constructor(private val saleApiService: SaleApiService) : BaseRepository() {
    fun getSalesList(userToken: String, page: Int, pageSize: Int) = call(saleApiService.getSalesList(userToken, page, pageSize))
    fun getSaleDetail(saleId: Int) = call((saleApiService.getSaleDetail(saleId)))
    fun uploadSale(sale: Sale) = call(saleApiService.uploadSale(sale))
    fun uploadSalePhoto(saleId: Int, photo: MultipartBody.Part) = call(saleApiService.uploaSalePhoto(saleId, photo))
    fun updateSale(sale: Sale) = call((saleApiService.uploadSale(sale)))
    fun deleteSale(saleId: Int) = call(saleApiService.deleteSale(saleId))

    fun likeSale(saleId: Int, userToken: String) = call(saleApiService.likeSale(saleId, userToken))
    fun disLikeSale(saleId: Int, userToken: String) = call(saleApiService.disLikeSale(saleId, userToken))
}