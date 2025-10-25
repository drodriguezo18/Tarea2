import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { IProduct, IProductList } from '../../../interfaces';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.scss'
})
export class ProductFormComponent {
  @Input() form!: FormGroup;
  @Input() isEdit: boolean = false;
  @Input() productLists: IProductList[] = [];
  @Input() showProductListSelector: boolean = true;
  @Output() callSaveMethod: EventEmitter<IProduct> = new EventEmitter<IProduct>();
}
