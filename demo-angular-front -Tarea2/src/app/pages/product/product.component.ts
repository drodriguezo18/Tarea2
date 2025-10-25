import { Component, effect, inject } from '@angular/core';
import { PaginationComponent } from '../../components/pagination/pagination.component';
import { ProductListService } from '../../services/product-list.service';
import { LoaderComponent } from '../../components/loader/loader.component';
import { ProductListComponent } from '../../components/product/product-list/product-list.component';
import { ProductListFormComponent } from '../../components/product/product-list-form/product-list-form.component';
import { FormBuilder, Validators } from '@angular/forms';
import { IProductList } from '../../interfaces';

@Component({
  selector: 'app-gift',
  standalone: true,
  imports: [
    PaginationComponent,
    LoaderComponent,
    ProductListComponent,
    ProductListFormComponent
  ],
  templateUrl: './product.component.html',
  styleUrl: './product.component.scss'
})
export class ProductComponent {
  public productListService: ProductListService = inject(ProductListService);
  public fb: FormBuilder = inject(FormBuilder);
  public form = this.fb.group({
    id: [0],
    name: ['', Validators.required],
    description: ['', Validators.required]
  })
  constructor() {
    this.productListService.getAll();
    effect(() => {
      console.log('product lists updated', this.productListService.productsLists$());
      if (this.productListService.productsLists$()[0]) {
        this.productListService.productsLists$()[0] ?  this.productListService.productsLists$()[0].name = `${this.productListService.productsLists$()[0].name} - Caros` : null;
      }
    });
  }

  save(item: IProductList) {
    item.id ? this.productListService.update(item) : this.productListService.save(item);
    this.form.reset();
  }

  delete(item: IProductList) {
    console.log('delete', item);
    this.productListService.delete(item);
  }
}