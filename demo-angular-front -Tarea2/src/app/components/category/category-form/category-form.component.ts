import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ICategory, ICategoryList } from '../../../interfaces';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './category-form.component.html',
  styleUrl: './category-form.component.scss'
})
export class CategoryFormComponent {
  @Input() form!: FormGroup;
  @Input() isEdit: boolean = false;
  @Input() categoryLists: ICategoryList[] = [];
  @Input() showCategoryListSelector: boolean = true;
  @Output() callSaveMethod: EventEmitter<ICategory> = new EventEmitter<ICategory>();
}
