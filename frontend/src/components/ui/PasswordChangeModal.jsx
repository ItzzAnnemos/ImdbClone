import { CheckCircle2, KeyRound, Loader2, X } from "lucide-react";
import { Button } from "./Button";

export function PasswordChangeModal({
    isOpen,
    form,
    saving,
    message,
    error,
    onChange,
    onClose,
    onSubmit,
}) {
    if (!isOpen) {
        return null;
    }

    return (
        <div
            className="fixed inset-0 z-[100] flex items-center justify-center bg-black/70 px-4 py-6 backdrop-blur-sm"
            onMouseDown={onClose}
        >
            <div
                className="w-full max-w-md rounded-xl border border-border bg-card shadow-2xl"
                onMouseDown={(event) => event.stopPropagation()}
            >
                <div className="flex items-start justify-between gap-4 border-b border-border px-5 py-4">
                    <div className="flex items-center gap-3">
                        <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-primary/10">
                            <KeyRound className="h-4 w-4 text-primary" />
                        </div>
                        <div>
                            <h2 className="text-lg font-semibold text-foreground">
                                Change Password
                            </h2>
                            <p className="text-sm text-muted-foreground">
                                Update your account password.
                            </p>
                        </div>
                    </div>
                    <button
                        type="button"
                        onClick={onClose}
                        disabled={saving}
                        className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full text-muted-foreground transition hover:bg-secondary hover:text-foreground disabled:opacity-50"
                        aria-label="Close password modal"
                    >
                        <X className="h-5 w-5" />
                    </button>
                </div>

                <form className="space-y-4 px-5 py-5" onSubmit={onSubmit}>
                    <div>
                        <label className="mb-1 block text-sm font-medium text-foreground">
                            Current password
                        </label>
                        <input
                            type="password"
                            value={form.currentPassword}
                            onChange={(event) => onChange("currentPassword", event.target.value)}
                            className="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm outline-none transition focus:border-primary focus:ring-1 focus:ring-primary"
                            required
                        />
                    </div>
                    <div>
                        <label className="mb-1 block text-sm font-medium text-foreground">
                            New password
                        </label>
                        <input
                            type="password"
                            value={form.newPassword}
                            onChange={(event) => onChange("newPassword", event.target.value)}
                            className="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm outline-none transition focus:border-primary focus:ring-1 focus:ring-primary"
                            required
                        />
                    </div>
                    <div>
                        <label className="mb-1 block text-sm font-medium text-foreground">
                            Confirm new password
                        </label>
                        <input
                            type="password"
                            value={form.repeatNewPassword}
                            onChange={(event) => onChange("repeatNewPassword", event.target.value)}
                            className="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm outline-none transition focus:border-primary focus:ring-1 focus:ring-primary"
                            required
                        />
                    </div>

                    {error && (
                        <p className="rounded-lg bg-destructive/10 px-3 py-2 text-sm text-destructive">
                            {error}
                        </p>
                    )}
                    {message && (
                        <p className="flex items-center gap-2 rounded-lg bg-primary/10 px-3 py-2 text-sm text-primary">
                            <CheckCircle2 className="h-4 w-4" />
                            {message}
                        </p>
                    )}

                    <div className="flex flex-col-reverse gap-3 pt-2 sm:flex-row sm:justify-end">
                        <Button type="button" variant="outline" onClick={onClose} disabled={saving}>
                            Cancel
                        </Button>
                        <Button
                            type="submit"
                            className="bg-yellow-400 font-bold text-black hover:bg-yellow-500"
                            disabled={saving}
                        >
                            {saving ? (
                                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                            ) : (
                                <KeyRound className="mr-2 h-4 w-4" />
                            )}
                            Save Password
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
}
