import { Component, effect, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/product.service';
import { ProductListService } from '../../services/product-list.service';
import { IProduct, IProductList } from '../../interfaces';
import { ProductFormComponent } from '../../components/product/product-form/product-form.component';
import { ProductsTableComponent } from '../../components/product/products-table/products-table.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { PaginationComponent } from '../../components/pagination/pagination.component';

@Component({
  selector: 'app-product-list-products',
  standalone: true,
  imports: [
    CommonModule,
    ProductFormComponent,
    ProductsTableComponent,
    PaginationComponent
  ],
  templateUrl: './product-list-products.component.html',
  styleUrls: ['./product-list-products.component.scss']
})
export class ProductListProductsComponent implements OnInit {
  public productService: ProductService = inject(ProductService);
  public rpoductListService: ProductListService = inject(ProductListService);
  public fb: FormBuilder = inject(FormBuilder);
  private route: ActivatedRoute = inject(ActivatedRoute);
  
  public productListId: number = 0;
  public currentPorductList?: IProductList;
  public isEdit: boolean = false;
  
  public form = this.fb.group({
    id: [0],
    name: ['', Validators.required],
    description: [''],
    price: [0],
    stock: ['']
  });

  constructor() {
    effect(() => {
      console.log('products updated', this.productService.products$());
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.productListId = +params['id'];
      if (this.productListId) {
        this.loadProductList();
        this.productService.getProductsByProductListId(this.productListId);
      }
    });
  }

  loadProductList() {
    // Load gift list details if needed
    // This would require adding a method to get a single gift list
  }

  loadProducts() {
    this.productService.getGiftsByGiftListId(this.productListId);
  }

  save(product: IProduct) {
    if (this.isEdit && product.id) {
      product.productList = { id: this.productListId };
      this.productService.update(product);
    } else {
      this.productService.addProducttoProductList(this.productListId, product);
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
      stock: product.stock || ''
    });
  }

  delete(product: IProduct) {
    if (product.id) {
      this.productService.deleteProductFromProductList(this.productListId, product.id);
    }
  }
}
