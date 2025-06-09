FROM python:3.9-slim

WORKDIR /app

COPY . /app

# 安装依赖包，建议用 --no-cache-dir 减小镜像体积
RUN pip install --no-cache-dir flask gunicorn torch numpy pandas scikit-learn joblib matplotlib

EXPOSE 7778

CMD ["gunicorn", "-w", "4", "-b", "0.0.0.0:7778", "model_web_api:app"]
