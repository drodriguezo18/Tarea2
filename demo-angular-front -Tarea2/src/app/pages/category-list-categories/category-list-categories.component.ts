import { Component, effect, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../services/category.service';
import { CategoryListService } from '../../services/category-list.service';
import { ICategory, ICategoryList } from '../../interfaces';
import { CategoryFormComponent } from '../../components/category/category-form/category-form.component';
import { CategoriesTableComponent } from '../../components/category/categories-table/categories-table.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { PaginationComponent } from '../../components/pagination/pagination.component';

@Component({
  selector: 'app-category-list-categories',
  standalone: true,
  imports: [
    CommonModule,
    CategoryFormComponent,
    CategoriesTableComponent,
    PaginationComponent
  ],
  templateUrl: './category-list-categories.component.html',
  styleUrls: ['./category-list-categories.component.scss']
})
export class CategoryListCategoriesComponent implements OnInit {
  public categoryService: CategoryService = inject(CategoryService);
  public categoryListService: CategoryListService = inject(CategoryListService);
  public fb: FormBuilder = inject(FormBuilder);
  private route: ActivatedRoute = inject(ActivatedRoute);
  
  public categoryListId: number = 0;
  public currentCategoryList?: ICategoryList;
  public isEdit: boolean = false;
  
  public form = this.fb.group({
    id: [0],
    name: ['', Validators.required],
    description: [''],
  });

  constructor() {
    effect(() => {
      console.log('categories updated', this.categoryService.categories$());
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.categoryListId = +params['id'];
      if (this.categoryListId) {
        this.loadCategoryList();
        this.categoryService.getCategoriesByCategoryListId(this.categoryListId);
      }
    });
  }

  loadCategoryList() {
  }

  loadCategories() {
    this.categoryService.getCategoriesByCategoryListId(this.categoryListId);
  }

  save(category: ICategory) {
    if (this.isEdit && category.id) {
      category.categoryList = { id: this.categoryListId };
      this.categoryService.update(category);
    } else {
      this.categoryService.addCategoryToCategoryList(this.categoryListId, category);
    }
    this.form.reset();
    this.isEdit = false;
  }

  edit(category: ICategory) {
    this.isEdit = true;
    this.form.patchValue({
      id: category.id || 0,
      name: category.name || '',
      description: category.description || '',
    });
  }

  delete(category: ICategory) {
    if (category.id) {
      this.categoryService.deleteCategoryFromCategoryList(this.categoryListId, category.id);
    }
  }
}
