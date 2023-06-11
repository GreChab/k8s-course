{{- define "k8s-helm-overview.labels" }}
  labels:
      date: {{ now | htmlDate }}
      version: "1.0.0"
{{- end }}
