import { inject, Injectable, signal } from '@angular/core';
import { IProductList, IResponse, ISearch } from '../interfaces';
import { BaseService } from './base-service';
import { AuthService } from './auth.service';
import { AlertService } from './alert.service';

@Injectable({
  providedIn: 'root'
})
export class ProductListService extends BaseService<IProductList> {
  protected override source: string = 'product-lists';
  private productListSignal = signal<IProductList[]>([]);
  get productsLists$() {
    return this.productListSignal;
  }
    public search: ISearch = { 
      page: 1,
      size: 5
    }
    public totalItems: any = [];
    private authService: AuthService = inject(AuthService);
    private alertService: AlertService = inject(AlertService);

    getAll() {
      this.findAllWithParams({ page: this.search.page, size: this.search.size}).subscribe({
        next: (response: IResponse<IProductList[]>) => {
          this.search = {...this.search, ...response.meta};
          this.totalItems = Array.from({length: this.search.totalPages ? this.search.totalPages: 0}, (_, i) => i+1);
          this.productListSignal.set(response.data);
        },
        error: (err: any) => {
          console.error('error', err);
        }
      });
    }

    save(item: IProductList) {
      this.add(item).subscribe({
        next: (response: IResponse<IProductList>) => {
          this.alertService.displayAlert('success', response.message, 'center', 'top', ['success-snackbar']);
          this.getAll();
        },
        error: (err: any) => {
          this.alertService.displayAlert('error', 'An error occurred adding the product list','center', 'top', ['error-snackbar']);
          console.error('error', err);
        }
      });
    }

    update(item: IProductList) {
      this.customEdit(item).subscribe({
        next: (response: IResponse<IProductList>) => {
          this.alertService.displayAlert('success', response.message, 'center', 'top', ['success-snackbar']);
          this.getAll();
        },
        error: (err: any) => {
          this.alertService.displayAlert('error', 'An error occurred updating the product list','center', 'top', ['error-snackbar']);
          console.error('error', err);
        }
      });
    }

    delete(item: IProductList) {
      this.del(item.id).subscribe({
        next: (response: IResponse<IProductList>) => {
          this.alertService.displayAlert('success', response.message, 'center', 'top', ['success-snackbar']);
          this.getAll();
        },
        error: (err: any) => {
          this.alertService.displayAlert('error', 'An error occurred deleting the product list', 'center', 'top', ['error-snackbar']);
          console.error('error', err);
        }
      });
    }
}
