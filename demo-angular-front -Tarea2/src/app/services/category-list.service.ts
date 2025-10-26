import { inject, Injectable, signal } from '@angular/core';
import { ICategoryList, IResponse, ISearch } from '../interfaces';
import { BaseService } from './base-service';
import { AuthService } from './auth.service';
import { AlertService } from './alert.service';

@Injectable({
  providedIn: 'root'
})
export class CategoryListService extends BaseService<ICategoryList> {
  protected override source: string = 'category-lists';
  private categoryListSignal = signal<ICategoryList[]>([]);
  get categoriesLists$() {
    return this.categoryListSignal;
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
        next: (response: IResponse<ICategoryList[]>) => {
          this.search = {...this.search, ...response.meta};
          this.totalItems = Array.from({length: this.search.totalPages ? this.search.totalPages: 0}, (_, i) => i+1);
          this.categoryListSignal.set(response.data);
        },
        error: (err: any) => {
          console.error('error', err);
        }
      });
    }

    save(item: ICategoryList) {
      this.add(item).subscribe({
        next: (response: IResponse<ICategoryList>) => {
          this.alertService.displayAlert('success', response.message, 'center', 'top', ['success-snackbar']);
          this.getAll();
        },
        error: (err: any) => {
          this.alertService.displayAlert('error', 'An error occurred adding the category list','center', 'top', ['error-snackbar']);
          console.error('error', err);
        }
      });
    }

    update(item: ICategoryList) {
      this.customEdit(item).subscribe({
        next: (response: IResponse<ICategoryList>) => {
          this.alertService.displayAlert('success', response.message, 'center', 'top', ['success-snackbar']);
          this.getAll();
        },
        error: (err: any) => {
          this.alertService.displayAlert('error', 'An error occurred updating the category list','center', 'top', ['error-snackbar']);
          console.error('error', err);
        }
      });
    }

    delete(item: ICategoryList) {
      this.del(item.id).subscribe({
        next: (response: IResponse<ICategoryList>) => {
          this.alertService.displayAlert('success', response.message, 'center', 'top', ['success-snackbar']);
          this.getAll();
        },
        error: (err: any) => {
          this.alertService.displayAlert('error', 'An error occurred deleting the category list', 'center', 'top', ['error-snackbar']);
          console.error('error', err);
        }
      });
    }
}
