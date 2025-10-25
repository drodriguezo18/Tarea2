import { Component, effect, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/product.service';
import { ProductListService } from '../../servicesproduct-list.service';
import { IProduct } from '../../interfaces';
import { ProductFormComponent } from '../../components/product/product-form/product-form.component';
import { ProductsTableComponent } from '../../components/product/products-table/products-table.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { PaginationComponent } from '../../components/pagination/pagination.component';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    CommonModule,
    ProductFormComponent,
    ProductsTableComponent,
    LoaderComponent,
    PaginationComponent
  ],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss'
})
export class ProductsComponent implements OnInit {
  public productService: ProductService = inject(ProductService);
  public productListService: ProductListService = inject(ProductListService);
  public fb: FormBuilder = inject(FormBuilder);
  public areActionsAvailable: boolean = false;
  public authService: AuthService = inject(AuthService);
  public route: ActivatedRoute = inject(ActivatedRoute);
  
  public isEdit: boolean = false;
  
  public form = this.fb.group({
    id: [0],
    name: ['', Validators.required],
    description: [''],
    price: [0],
    imageUrl: [''],
    giftListId: ['', Validators.required]
  });

  constructor() {
    effect(() => {
      console.log('products updated', this.productService.products$());
    });
  }

  ngOnInit() {
    this.productListService.getAll();
    this.productService.getAll();
    this.route.data.subscribe( data => {
      this.areActionsAvailable = this.authService.areActionsAvailable(data['authorities'] ? data['authorities'] : []);
      console.log('areActionsAvailable', this.areActionsAvailable);
    });
  }

  save(product: IProduct) {
    const productListId = this.form.get('productListId')?.value;
    
    if (this.isEdit && product.id) {
      product.productList = { id: Number(productListId) };
      this.productService.update(product);
    } else {
      if (productListId) {
        this.productService.addProductToProductList(Number(productListId), product);
      }
    }
    this.form.reset();
    this.isEdit = false;
  }

  edit(product: IProduct) {
    this.isEdit = true;
    this.form.patchValue({
      id: product.id || 0,
      name: product.name || '',
      description: product.description || '',
      price: product.price || 0,
      stock: product.stock || '',
      productListId: product.productList?.id?.toString() || ''
    });
  }

  delete(product: IProduct) {
    if (product.id && product.productList?.id) {
      this.productService.deleteProductFromProductList(product.productList.id, product.id);
    } else if (product.id) {
      this.productService.delete(product);
    }
  }
}
