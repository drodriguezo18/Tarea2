import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IProductList } from '../../../interfaces';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [],
  templateUrl: './product-list.component.html',
  styleUrl: './giproductft-list.component.scss'
})
export class ProductListComponent {
  @Input() productsList: IProductList[] = [];
  @Output() callEditMethod: EventEmitter<IProductList> = new EventEmitter<IProductList>();
  @Output() callDeleteMethod: EventEmitter<IProductList> = new EventEmitter<IProductList>();
}
