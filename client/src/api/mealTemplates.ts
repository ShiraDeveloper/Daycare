import { apiClient } from './client'
import type { MealTemplateDto, MealType } from '../types'

export async function fetchMealTemplates(
  type?: MealType,
): Promise<MealTemplateDto[]> {
  const { data } = await apiClient.get<MealTemplateDto[]>(
    '/api/meal-templates',
    { params: type ? { type } : undefined },
  )
  return data
}
