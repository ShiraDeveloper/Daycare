import { useCallback, useEffect, useState } from 'react'
import { fetchTodayBoard } from '../api/attendance'
import { extractErrorMessage } from '../api/client'
import { ChildCard } from '../components/dashboard/ChildCard'
import { Header } from '../components/layout/Header'
import { Toast } from '../components/ui/Toast'
import { WorkflowModal } from '../components/workflow/WorkflowModal'
import type { DailyAttendanceDto } from '../types'

export function DashboardPage() {
  const [children, setChildren] = useState<DailyAttendanceDto[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedChild, setSelectedChild] = useState<DailyAttendanceDto | null>(null)
  const [modalOpen, setModalOpen] = useState(false)
  const [toast, setToast] = useState<{ message: string; visible: boolean }>({
    message: '',
    visible: false,
  })

  const loadBoard = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await fetchTodayBoard()
      setChildren(data)
    } catch (err) {
      setError(extractErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    loadBoard()
  }, [loadBoard])

  const openWorkflow = (child: DailyAttendanceDto) => {
    setSelectedChild(child)
    setModalOpen(true)
  }

  const handleUpdated = (updated: DailyAttendanceDto) => {
    setChildren((prev) =>
      prev.map((c) => (c.childId === updated.childId ? updated : c)),
    )
    setSelectedChild(updated)
  }

  const showSuccess = (message: string) => {
    setToast({ message, visible: true })
    setTimeout(() => setToast({ message: '', visible: false }), 2800)
  }

  const pendingCount = children.filter(
    (c) => c.nextStep !== 'DONE' && c.workflowState !== 'COMPLETED',
  ).length

  return (
    <div className="min-h-screen bg-canvas">
      <Header />

      <main className="mx-auto max-w-6xl px-4 py-8 sm:px-6">
        <div className="mb-8 flex flex-wrap items-end justify-between gap-4">
          <div>
            <p className="text-sm text-muted">
              {children.length} children · {pendingCount} need attention
            </p>
          </div>
          <button
            type="button"
            onClick={loadBoard}
            disabled={loading}
            className="rounded-xl border border-border bg-white px-4 py-2 text-sm font-medium text-slate-600 shadow-sm hover:bg-slate-50 disabled:opacity-50"
          >
            {loading ? 'Refreshing…' : 'Refresh'}
          </button>
        </div>

        {error && (
          <div className="mb-6 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        {loading && children.length === 0 ? (
          <div className="flex justify-center py-20">
            <div className="h-10 w-10 animate-spin rounded-full border-4 border-brand-200 border-t-brand-600" />
          </div>
        ) : children.length === 0 ? (
          <div className="rounded-2xl border border-dashed border-border bg-white py-16 text-center">
            <p className="text-muted">No children registered yet.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {children.map((child) => (
              <ChildCard
                key={child.childId}
                child={child}
                onClick={() => openWorkflow(child)}
              />
            ))}
          </div>
        )}
      </main>

      <WorkflowModal
        child={selectedChild}
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        onUpdated={handleUpdated}
        onSuccess={showSuccess}
      />

      <Toast message={toast.message} visible={toast.visible} />
    </div>
  )
}
