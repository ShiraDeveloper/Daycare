import { useCallback, useEffect, useState } from 'react'
import {
  markPresent,
  recordActivities,
  recordBreakfast,
  recordGuestParticipation,
  recordLunch,
} from '../../api/attendance'
import { fetchMealTemplates } from '../../api/mealTemplates'
import { extractErrorMessage } from '../../api/client'
import type {
  ActivityType,
  DailyActivityDto,
  DailyAttendanceDto,
  MealTemplateDto,
} from '../../types'
import { ACTIVITY_OPTIONS, isWorkflowDone, NEXT_STEP_LABELS } from '../../utils/workflow'
import { Button } from '../ui/Button'
import { Modal } from '../ui/Modal'
import { TextArea } from '../ui/TextArea'

interface WorkflowModalProps {
  child: DailyAttendanceDto | null
  open: boolean
  onClose: () => void
  onUpdated: (updated: DailyAttendanceDto) => void
  onSuccess: (message: string) => void
}

export function WorkflowModal({
  child,
  open,
  onClose,
  onUpdated,
  onSuccess,
}: WorkflowModalProps) {
  const [current, setCurrent] = useState<DailyAttendanceDto | null>(child)
  const [remarks, setRemarks] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  // Meal step state
  const [breakfastTemplates, setBreakfastTemplates] = useState<MealTemplateDto[]>([])
  const [lunchTemplates, setLunchTemplates] = useState<MealTemplateDto[]>([])
  const [selectedMealId, setSelectedMealId] = useState<number | ''>('')

  // Activities step state
  const [selectedActivities, setSelectedActivities] = useState<ActivityType[]>([])
  const [activityDescription, setActivityDescription] = useState('')

  // Guest participation
  const [participated, setParticipated] = useState(true)

  useEffect(() => {
    setCurrent(child)
    setRemarks('')
    setError(null)
    setSelectedMealId('')
    setSelectedActivities([])
    setActivityDescription('')
    setParticipated(true)
  }, [child, open])

  useEffect(() => {
    if (!open || !current) return
    if (current.nextStep === 'RECORD_BREAKFAST') {
      fetchMealTemplates('BREAKFAST')
        .then(setBreakfastTemplates)
        .catch(() => setBreakfastTemplates([]))
    }
    if (current.nextStep === 'RECORD_LUNCH') {
      fetchMealTemplates('LUNCH')
        .then(setLunchTemplates)
        .catch(() => setLunchTemplates([]))
    }
  }, [open, current?.nextStep, current])

  const runStep = useCallback(
    async (action: () => Promise<DailyAttendanceDto>, successMsg: string) => {
      if (!current) return
      setLoading(true)
      setError(null)
      try {
        const updated = await action()
        setCurrent(updated)
        onUpdated(updated)
        setRemarks('')
        setSelectedMealId('')
        onSuccess(successMsg)
        if (isWorkflowDone(updated)) {
          setTimeout(onClose, 800)
        }
      } catch (err) {
        setError(extractErrorMessage(err))
      } finally {
        setLoading(false)
      }
    },
    [current, onClose, onSuccess, onUpdated],
  )

  if (!current) return null

  const step = current.nextStep
  const stepLabel = NEXT_STEP_LABELS[step] ?? step

  const renderStepContent = () => {
    if (step === 'DONE' || step === 'COMPLETE') {
      return (
        <div className="py-8 text-center">
          <div className="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-emerald-100 text-2xl">
            ✓
          </div>
          <p className="font-semibold text-slate-800">All steps complete for today.</p>
          {current.breakfastName && (
            <p className="mt-2 text-sm text-muted">Breakfast: {current.breakfastName}</p>
          )}
          {current.lunchName && (
            <p className="text-sm text-muted">Lunch: {current.lunchName}</p>
          )}
        </div>
      )
    }

    if (step === 'MARK_PRESENCE') {
      return (
        <form
          onSubmit={(e) => {
            e.preventDefault()
            runStep(
              () => markPresent(current.childId, { remarks: remarks || undefined }),
              `${current.childName} marked present`,
            )
          }}
          className="space-y-4"
        >
          <p className="text-sm text-muted">
            Confirm that {current.childName} has arrived and is present today.
          </p>
          <TextArea
            label="Remarks (optional)"
            placeholder="Any notes about arrival..."
            value={remarks}
            onChange={(e) => setRemarks(e.target.value)}
          />
          <Button type="submit" loading={loading} className="w-full">
            Mark present
          </Button>
        </form>
      )
    }

    if (step === 'RECORD_BREAKFAST' || step === 'RECORD_LUNCH') {
      const templates = step === 'RECORD_BREAKFAST' ? breakfastTemplates : lunchTemplates
      const label = step === 'RECORD_BREAKFAST' ? 'Breakfast' : 'Lunch'
      const submit = () =>
        runStep(
          () =>
            step === 'RECORD_BREAKFAST'
              ? recordBreakfast(current.childId, {
                  mealTemplateId: Number(selectedMealId),
                  remarks: remarks || undefined,
                })
              : recordLunch(current.childId, {
                  mealTemplateId: Number(selectedMealId),
                  remarks: remarks || undefined,
                }),
          `${label} recorded for ${current.childName}`,
        )

      return (
        <form
          onSubmit={(e) => {
            e.preventDefault()
            if (!selectedMealId) {
              setError('Please select a meal option')
              return
            }
            submit()
          }}
          className="space-y-4"
        >
          <p className="text-sm text-muted">Select today&apos;s {label.toLowerCase()} choice.</p>
          <div className="grid gap-2">
            {templates.length === 0 ? (
              <p className="rounded-xl border border-dashed border-border p-4 text-sm text-muted">
                No {label.toLowerCase()} templates yet. Add them in the admin panel.
              </p>
            ) : (
              templates.map((t) => (
                <label
                  key={t.id}
                  className={`flex cursor-pointer items-center gap-3 rounded-xl border p-4 transition ${
                    selectedMealId === t.id
                      ? 'border-brand-500 bg-brand-50 ring-2 ring-brand-500/20'
                      : 'border-border hover:border-brand-200'
                  }`}
                >
                  <input
                    type="radio"
                    name="meal"
                    value={t.id}
                    checked={selectedMealId === t.id}
                    onChange={() => setSelectedMealId(t.id)}
                    className="accent-brand-600"
                  />
                  <div>
                    <p className="font-semibold text-slate-800">{t.name}</p>
                    {t.description && (
                      <p className="text-xs text-muted">{t.description}</p>
                    )}
                  </div>
                </label>
              ))
            )}
          </div>
          <TextArea
            label="Remarks (optional)"
            value={remarks}
            onChange={(e) => setRemarks(e.target.value)}
          />
          <Button type="submit" loading={loading} disabled={templates.length === 0} className="w-full">
            Save {label.toLowerCase()}
          </Button>
        </form>
      )
    }

    if (step === 'RECORD_ACTIVITIES') {
      const toggleActivity = (type: ActivityType) => {
        setSelectedActivities((prev) =>
          prev.includes(type) ? prev.filter((a) => a !== type) : [...prev, type],
        )
      }

      return (
        <form
          onSubmit={(e) => {
            e.preventDefault()
            if (selectedActivities.length === 0) {
              setError('Select at least one activity')
              return
            }
            const activities: DailyActivityDto[] = selectedActivities.map((type) => ({
              activityType: type,
              description: activityDescription || undefined,
              remarks: remarks || undefined,
            }))
            runStep(
              () =>
                recordActivities(current.childId, {
                  activities,
                  remarks: remarks || undefined,
                }),
              `Activities saved for ${current.childName}`,
            )
          }}
          className="space-y-4"
        >
          <p className="text-sm text-muted">What did {current.childName} do today?</p>
          <div className="flex flex-wrap gap-2">
            {ACTIVITY_OPTIONS.map((type) => (
              <button
                key={type}
                type="button"
                onClick={() => toggleActivity(type)}
                className={`rounded-full px-3 py-1.5 text-xs font-semibold transition ${
                  selectedActivities.includes(type)
                    ? 'bg-brand-600 text-white'
                    : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                }`}
              >
                {type.replace('_', ' ')}
              </button>
            ))}
          </div>
          <TextArea
            label="Activity notes (optional)"
            placeholder="Describe what happened..."
            value={activityDescription}
            onChange={(e) => setActivityDescription(e.target.value)}
          />
          <TextArea
            label="Remarks (optional)"
            value={remarks}
            onChange={(e) => setRemarks(e.target.value)}
          />
          <Button type="submit" loading={loading} className="w-full">
            Save activities
          </Button>
        </form>
      )
    }

    if (step === 'RECORD_GUEST_PARTICIPATION') {
      return (
        <form
          onSubmit={(e) => {
            e.preventDefault()
            runStep(
              () =>
                recordGuestParticipation(current.childId, {
                  participated,
                  remarks: remarks || undefined,
                }),
              `Guest participation recorded for ${current.childName}`,
            )
          }}
          className="space-y-4"
        >
          <p className="text-sm text-muted">
            A guest performer is scheduled today. Did {current.childName} participate?
          </p>
          <label className="flex cursor-pointer items-center gap-3 rounded-xl border border-border p-4">
            <input
              type="checkbox"
              checked={participated}
              onChange={(e) => setParticipated(e.target.checked)}
              className="h-5 w-5 rounded accent-brand-600"
            />
            <span className="font-medium text-slate-800">
              Child participated in the guest performance
            </span>
          </label>
          <TextArea
            label="Remarks (optional)"
            value={remarks}
            onChange={(e) => setRemarks(e.target.value)}
          />
          <Button type="submit" loading={loading} className="w-full">
            Confirm participation
          </Button>
        </form>
      )
    }

    return null
  }

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={current.childName}
      subtitle={stepLabel}
    >
      {error && (
        <div className="mb-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {error}
        </div>
      )}
      {renderStepContent()}
    </Modal>
  )
}
